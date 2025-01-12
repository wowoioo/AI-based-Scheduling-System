package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.Professor;
import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.Cohort;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.entity.Timeslot;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GAService {
    public List<InputData> jgap(List<InputData> inputDataList, List<Cohort> cohortList, List<Timeslot> timeslotList,List<Classroom> classroomList) {
        IntStream.range(0, inputDataList.size()).forEach(i -> inputDataList.get(i).setId((long) i));
        IntStream.range(0, cohortList.size()).forEach(i -> cohortList.get(i).setCohortId(i));
        IntStream.range(0, timeslotList.size()).forEach(i -> timeslotList.get(i).setTimeslotId(i));
        Map<String, List<InputData>> groupByCohortData =
                inputDataList.stream().collect(Collectors.groupingBy(InputData::getCohort));
        for (Cohort cohort : cohortList) {
            if (groupByCohortData.containsKey(cohort.getCohort())) {
                int[] courseIds =
                        groupByCohortData.get(cohort.getCohort()).stream().map(InputData::getId).mapToInt(Long::intValue).toArray();
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
        return Collections.emptyList();
    }
}
