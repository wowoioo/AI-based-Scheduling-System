package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.GAcourse.TeachingPlan;
import com.github.rayinfinite.scheduler.GAcourse.TimatableGA;
import com.github.rayinfinite.scheduler.GAcourse.Timetable;
import com.github.rayinfinite.scheduler.GAcourse.TimetableOutput;
import com.github.rayinfinite.scheduler.GAcourse.config.GA;
import com.github.rayinfinite.scheduler.GAcourse.config.Population;
import com.github.rayinfinite.scheduler.entity.Professor;
import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.Cohort;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.entity.Timeslot;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.rayinfinite.scheduler.GAcourse.TimatableGA.convertToDate;

@Service
public class GAService {
    public List<InputData> jgap(List<InputData> inputDataList, List<Cohort> cohortList, List<Timeslot> timeslotList,List<Classroom> classroomList) {
        IntStream.range(0, inputDataList.size()).forEach(i -> inputDataList.get(i).setId(i));
        IntStream.range(0, cohortList.size()).forEach(i -> cohortList.get(i).setCohortId(i));
        IntStream.range(0, timeslotList.size()).forEach(i -> timeslotList.get(i).setTimeslotId(i));
        Map<String, List<InputData>> groupByCohortData =
                inputDataList.stream().collect(Collectors.groupingBy(InputData::getCohort));
        for (Cohort cohort : cohortList) {
            if (groupByCohortData.containsKey(cohort.getCohort())) {
                int[] courseIds = groupByCohortData.get(cohort.getCohort()).stream()
                        .map(InputData::getId)
                        .mapToInt(id -> id)
                        .toArray();
                cohort.setCourseIds(courseIds);
            }
        }
        var teacherMap = getProfessorMap(inputDataList);
        Map<Integer, InputData> inputDataMap =
                inputDataList.stream().collect(Collectors.toMap(inputData -> inputData.getId().intValue(),
                        Function.identity()));
        Map<Integer, Cohort> cohortMap = cohortList.stream().collect(Collectors.toMap(Cohort::getCohortId,
                Function.identity()));
        Map<Integer, Timeslot> timeslotMap = timeslotList.stream().collect(Collectors.toMap(Timeslot::getTimeslotId,
                Function.identity()));
        Map<Integer, Classroom> classroomMap =
                classroomList.stream().collect(Collectors.toMap(classroom -> classroom.getId().intValue(),
                        Function.identity()));

        return getSchedule(inputDataMap, cohortMap, timeslotMap, classroomMap, teacherMap);
    }

    private Map<Integer,Professor> getProfessorMap(List<InputData> inputDataList){
        int i=0;
        Map<String,Integer> teacherMap = new HashMap<>();
        for (InputData data : inputDataList) {
            List<Integer> teachers = new ArrayList<>();
            if (data.getTeacher1() != null && !data.getTeacher1().isEmpty() ) {
                if(teacherMap.containsKey(data.getTeacher1())){
                    teachers.add(teacherMap.get(data.getTeacher1()));
                }else{
                    teachers.add(i);
                    teacherMap.put(data.getTeacher1(),i++);
                }
            }
            if (data.getTeacher2() != null && !data.getTeacher2().isEmpty() ) {
                if(teacherMap.containsKey(data.getTeacher2())){
                    teachers.add(teacherMap.get(data.getTeacher2()));
                }else{
                    teachers.add(i);
                    teacherMap.put(data.getTeacher2(),i++);
                }
            }
            if (data.getTeacher3() != null && !data.getTeacher3().isEmpty() ) {
                if(teacherMap.containsKey(data.getTeacher3())){
                    teachers.add(teacherMap.get(data.getTeacher3()));
                }else{
                    teachers.add(i);
                    teacherMap.put(data.getTeacher3(),i++);
                }
            }
            data.setTeacherIds(teachers.stream().mapToInt(Integer::intValue).toArray());
        }
        return teacherMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue,
                entry -> new Professor(entry.getValue(), entry.getKey())));
    }

    public List<InputData> getSchedule(Map<Integer, InputData> inputDataMap,
                                       Map<Integer, Cohort> cohortMap,
                                       Map<Integer, Timeslot> timeslotMap,
                                       Map<Integer, Classroom> classroomMap,
                                       Map<Integer, Professor> professorMap) {
        Timetable timetable = convertToTimetable(
                new ArrayList<>(inputDataMap.values()),
                new ArrayList<>(cohortMap.values()),
                new ArrayList<>(timeslotMap.values()),
                new ArrayList<>(classroomMap.values()),
                new ArrayList<>(professorMap.values())
        );

        // 初始化 GA
        GA ga = new GA(100, 0.001, 0.98, 1, 5);
        Population population = ga.initPopulation(timetable);
        int generation = 1;

        while (!ga.isTerminationConditionMet1(population) &&
                !ga.isTerminationconditionMet2(generation, TimatableGA.maxGenerations)) {
            population = ga.crossoverPopulation(population);
            population = ga.mutatePopulation(population, timetable);
            ga.evalPopulation(population, timetable);
            generation++;
        }

        timetable.createPlans(population.getFittest(0));
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Clashes: " + timetable.calcClashes());

        List<InputData> inputDataList = new ArrayList<>();
        TeachingPlan[] plans = timetable.getPlans();

        // 生成 List<InputData>
        for (TeachingPlan bestPlan : plans) {
            int courseId = bestPlan.getCourseId();
            String practiceArea = timetable.getCourse(courseId).getPracticeArea();
            String courseName = timetable.getCourse(courseId).getCourseName();
            String courseCode = timetable.getCourse(courseId).getCourseCode();
            int duration = timetable.getCourse(courseId).getDuration();
            int cohortId = bestPlan.getCohortId();
            String software = timetable.getCourse(courseId).getSoftware();
            int run = timetable.getCourse(courseId).getRun();
            String courseManager = timetable.getCourse(courseId).getCourseManager();
            String gradCert = timetable.getCourse(courseId).getGradCert();
            String cohort = timetable.getCohort(cohortId).getCohort();
            String classroom = timetable.getRoom(bestPlan.getRoomId()).getRoomNumber();

            // 获取时间信息
            Date timeslotDate = timetable.getTimeslot(bestPlan.getTimeslotId()).getTimeslot();
            // 获取教师 ID 列表
            int professorNum = timetable.getCourse(courseId).getProfessorNum();

            int[] teacherIds = new int[professorNum];
            teacherIds[0] = bestPlan.getProfessor1Id();
            if (professorNum > 1) {
                teacherIds[1] = bestPlan.getProfessor2Id();
            }
            if (professorNum > 2) {
                teacherIds[2] = bestPlan.getProfessor3Id();
            }

            // 获取教师名称
            String teacher1 = teacherIds.length > 0 && teacherIds[0] != -1
                    ? timetable.getProfessor(teacherIds[0]).getProfessorName()
                    : null;
            String teacher2 = teacherIds.length > 1 && teacherIds[1] != -1
                    ? timetable.getProfessor(teacherIds[1]).getProfessorName()
                    : null;
            String teacher3 = teacherIds.length > 2 && teacherIds[2] != -1
                    ? timetable.getProfessor(teacherIds[2]).getProfessorName()
                    : null;

            // 构建 InputData 对象
            InputData inputData = new InputData(
                    courseId, practiceArea, courseName, courseCode, duration, cohortId,
                    software, run, courseManager, gradCert, teacherIds, professorNum
            );

            // 设置教师名称
            inputData.setTeacher1(teacher1);
            inputData.setTeacher2(teacher2);
            inputData.setTeacher3(teacher3);
            inputData.setClassroom(classroom);
            inputData.setCourseDate(timeslotDate);
            inputData.setCohort(cohort);
            inputDataList.add(inputData);
        }

        IntStream.range(0, inputDataList.size()).forEach(i -> inputDataList.get(i).setId(i + 1));

        return inputDataList;
    }


    private Timetable convertToTimetable(List<InputData> inputDataList,
                                         List<Cohort> cohortList,
                                         List<Timeslot> timeslotList,
                                         List<Classroom> classroomList,
                                         List<Professor> professorList) {
        Timetable timetable = new Timetable();

        // 添加教室信息
        for (Classroom classroom : classroomList) {
            timetable.addRoom(classroom.getId(), classroom.getName(), classroom.getRoomCapacity());
        }

        // 添加时间段信息
        for (Timeslot timeslot : timeslotList) {
            timetable.addTimeslot(timeslot.getTimeslotId(), timeslot.getTimeslot());
        }

        // 添加教师信息
//        Map<Integer, Professor> professorMap = getProfessorMap(inputDataList);
//        for (Professor professor : professorMap.values()) {
//            timetable.addProfessor(professor.getProfessorId(), professor.getProfessorName());
//        }

        // 添加课程信息
        for (InputData data : inputDataList) {
            timetable.addCourse(data);
        }

        // 添加学生群体信息
        for (Cohort cohort : cohortList) {
            timetable.addCohort(cohort.getCohortId(), cohort.getCohort(), cohort.getCohortSize(), cohort.getTypeId(),
                    cohort.getCohortType());
        }

        for (Professor professor : professorList) {
            timetable.addProfessor(professor.getProfessorId(), professor.getProfessorName());
        }

        return timetable;
    }
}
