package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.ga_course.TeachingPlan;
import com.github.rayinfinite.scheduler.ga_course.Timetable;
import com.github.rayinfinite.scheduler.ga_course.config.GA;
import com.github.rayinfinite.scheduler.ga_course.config.Population;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class GAService {
    public List<Course> jgap(List<Course> courseList, List<Cohort> cohortList, List<Timeslot> timeslotList,
                             List<Classroom> classroomList) {
        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i));
        IntStream.range(0, cohortList.size()).forEach(i -> cohortList.get(i).setCohortId(i));
        IntStream.range(0, timeslotList.size()).forEach(i -> timeslotList.get(i).setTimeslotId(i));
        Map<String, List<Course>> groupByCohortData =
                courseList.stream().collect(Collectors.groupingBy(Course::getCohort));
        for (Cohort cohort : cohortList) {
            if (groupByCohortData.containsKey(cohort.getCohort())) {
                int[] courseIds = groupByCohortData.get(cohort.getCohort()).stream()
                        .map(Course::getId)
                        .mapToInt(id -> id)
                        .toArray();
                cohort.setCourseIds(courseIds);
            }
        }
        var teacherMap = getProfessorMap(courseList);
        Map<Integer, Course> inputDataMap = courseList.stream().collect(Collectors.toMap(Course::getId,
                Function.identity()));
        Map<Integer, Cohort> cohortMap = cohortList.stream().collect(Collectors.toMap(Cohort::getCohortId,
                Function.identity()));
        Map<Integer, Timeslot> timeslotMap = timeslotList.stream().collect(Collectors.toMap(Timeslot::getTimeslotId,
                Function.identity()));
        Map<Integer, Classroom> classroomMap = classroomList.stream().collect(Collectors.toMap(Classroom::getId,
                Function.identity()));

        return getSchedule(inputDataMap, cohortMap, timeslotMap, classroomMap, teacherMap);
    }

    private Map<Integer, Professor> getProfessorMap(List<Course> courseList) {
        int i = 0;
        Map<String, Integer> teacherMap = new HashMap<>();
        for (Course data : courseList) {
            List<Integer> teachers = new ArrayList<>();
            if (data.getTeacher1() != null && !data.getTeacher1().isEmpty()) {
                if (teacherMap.containsKey(data.getTeacher1())) {
                    teachers.add(teacherMap.get(data.getTeacher1()));
                } else {
                    teachers.add(i);
                    teacherMap.put(data.getTeacher1(), i++);
                }
            }
            if (data.getTeacher2() != null && !data.getTeacher2().isEmpty()) {
                if (teacherMap.containsKey(data.getTeacher2())) {
                    teachers.add(teacherMap.get(data.getTeacher2()));
                } else {
                    teachers.add(i);
                    teacherMap.put(data.getTeacher2(), i++);
                }
            }
            if (data.getTeacher3() != null && !data.getTeacher3().isEmpty()) {
                if (teacherMap.containsKey(data.getTeacher3())) {
                    teachers.add(teacherMap.get(data.getTeacher3()));
                } else {
                    teachers.add(i);
                    teacherMap.put(data.getTeacher3(), i++);
                }
            }
            data.setTeacherIds(teachers.stream().mapToInt(Integer::intValue).toArray());
        }
        return teacherMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue,
                entry -> new Professor(entry.getValue(), entry.getKey())));
    }

    public List<Course> getSchedule(Map<Integer, Course> inputDataMap,
                                    Map<Integer, Cohort> cohortMap,
                                    Map<Integer, Timeslot> timeslotMap,
                                    Map<Integer, Classroom> classroomMap,
                                    Map<Integer, Professor> professorMap) {
        Timetable timetable = new Timetable(inputDataMap, cohortMap, timeslotMap, classroomMap, professorMap);

        // 初始化 GA
        int maxGenerations = 1000;
        GA ga = new GA(100, 0.001, 0.98, 1, 5);
        Population population = ga.initPopulation(timetable);
        int generation = 1;

        while (!ga.isTerminationConditionMet1(population) &&
                !ga.isTerminationconditionMet2(generation, maxGenerations)) {
            population = ga.crossoverPopulation(population);
            population = ga.mutatePopulation(population, timetable);
            ga.evalPopulation(population, timetable);
            generation++;
        }

        timetable.createPlans(population.getFittest(0));
        log.info("Solution found in {} generations", generation);
        log.info("Final solution fitness: {}", population.getFittest(0).getFitness());
        log.info("Clashes: {}", timetable.calcClashes());

        List<Course> courseList = new ArrayList<>();

        // 生成 List<InputData>
        for (TeachingPlan bestPlan : timetable.getPlans()) {
            int courseId = bestPlan.getCourseId();
            Course course = new Course();
            BeanUtils.copyProperties(timetable.getCourse(courseId), course);

            course.setClassroom(timetable.getRoom(bestPlan.getRoomId()).getName());
            course.setCourseDate(timetable.getTimeslot(bestPlan.getTimeslotId()).getDate());

            courseList.add(course);
        }

        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i + 1));

        return courseList;
    }
}
