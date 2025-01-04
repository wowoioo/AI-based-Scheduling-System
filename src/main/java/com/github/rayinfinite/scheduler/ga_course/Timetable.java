package com.github.rayinfinite.scheduler.ga_course;

import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.ga_course.config.Individual;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Getter
public class Timetable {
    @Getter
    private final Map<Integer, Classroom> rooms;
    private final Map<Integer, Course> courses;
    private final Map<Integer, Cohort> cohorts;
    private final Map<Integer, Timeslot> timeslots;
    private final Map<Integer, Professor> professors;

    @Getter
    private TeachingPlan[] plans;

    private int plansNum = 0;

    //浅拷贝，用于适应度计算
    public Timetable(Timetable cloneable) {
        this.rooms = cloneable.getRooms();
        this.courses = cloneable.getCourses();
        this.cohorts = cloneable.getCohorts();
        this.timeslots = cloneable.getTimeslots();
        this.professors = cloneable.getProfessors();
    }

    public Timetable(Map<Integer, Course> inputDataMap,
                     Map<Integer, Cohort> cohortMap,
                     Map<Integer, Timeslot> timeslotMap,
                     Map<Integer, Classroom> classroomMap,
                     Map<Integer, Professor> professorMap) {
        this.rooms = classroomMap;
        this.courses = inputDataMap;
        this.cohorts = cohortMap;
        this.timeslots = timeslotMap;
        this.professors = professorMap;
        this.plansNum = 0;
    }

    public void createPlans(Individual individual) {
        int totalPlans = 0;

        // Calculate total plans based on InputData
        for (Course course : this.courses.values()) {
            totalPlans += course.getDuration();
        }
//        for (InputData course : this.courses.values()) {
//            totalPlans += course.getRun();
//        }
        TeachingPlan[] plans = new TeachingPlan[totalPlans];

        int[] chromosome = individual.getChromosome();
        int chromosomePos = 0;
        int planIndex = 0;

        // Iterate through cohorts and their corresponding courses
        for (Cohort cohort : this.getCohortsAsArray()) {
            for (Course course : this.getCoursesByCohortId(cohort.getCohortId())) {
                int duration = course.getDuration();

                for (int dayOffset = 0; dayOffset < duration; dayOffset++) {
                    plans[planIndex] = new TeachingPlan(planIndex, cohort.getCohortId(), course.getId());

                    int timeslotId = chromosome[chromosomePos] + dayOffset;
                    if (timeslotId > this.getMaxTimeslotId()) {
                        timeslotId = chromosome[chromosomePos];
                    }
                    plans[planIndex].addTimeslot(timeslotId);

                    if (dayOffset == 0) {
                        plans[planIndex].setRoomId(chromosome[chromosomePos + 1]);
                    } else {
                        plans[planIndex].setRoomId(plans[planIndex - 1].getRoomId());
                    }

                    if (dayOffset == 0) {
                        int[] teacherIds = course.getTeacherIds();
                        plans[planIndex].addProfessor1(teacherIds.length > 0 ? teacherIds[0] : 0);
                        plans[planIndex].addProfessor2(teacherIds.length > 1 ? teacherIds[1] : 0);
                        plans[planIndex].addProfessor3(teacherIds.length > 2 ? teacherIds[2] : 0);
                    } else {
                        plans[planIndex].addProfessor1(plans[planIndex - 1].getProfessor1Id());
                        plans[planIndex].addProfessor2(plans[planIndex - 1].getProfessor2Id());
                        plans[planIndex].addProfessor3(plans[planIndex - 1].getProfessor3Id());
                    }

                    planIndex++;
                }
                chromosomePos += 5;
            }
        }
        this.plans = plans;
    }

    public List<Course> getCoursesByCohortId(int cohortId) {
        List<Course> cohortCourses = new ArrayList<>();
        for (Course course : this.courses.values()) {
            if (course.getCohortId() == cohortId) {
                cohortCourses.add(course);
            }
        }
        return cohortCourses;
    }

    public int getMaxTimeslotId() {
        Set<Integer> timeslotIds = this.timeslots.keySet();
        if (timeslotIds.isEmpty()) {
            return -1;
        }
        return Collections.max(timeslotIds);
    }

    public Classroom getRoom(int roomId) {
        if (!this.rooms.containsKey(roomId)) {
            log.info("Rooms doesn't contain key {}", roomId);
        }
        return this.rooms.get(roomId);
    }

    public Classroom getRandomRoom() {
        Object[] roomsArray = this.rooms.values().toArray();
        return (Classroom) roomsArray[(int) (roomsArray.length * Math.random())];
    }

    public Course getCourse(int courseId) {
        return this.courses.get(courseId);
    }

    public Cohort getCohort(int cohortId) {
        return this.cohorts.get(cohortId);
    }

    public Cohort[] getCohortsAsArray() {
        return this.cohorts.values().toArray(new Cohort[0]);
    }

    public Timeslot getTimeslot(int timeslotId) {
        return this.timeslots.get(timeslotId);
    }

    public Timeslot getRandomTimeslot() {
        Object[] timeslotArray = this.timeslots.values().toArray();
        return (Timeslot) timeslotArray[(int) (timeslotArray.length * Math.random())];
    }

    public int getPlansNum(Timetable timetable) {
        return timetable.getCourses().size();
    }

    public int calcClashes() {
        int clashes = 0;

        //根据roomId和timeslotId分组
        Map<Integer, List<TeachingPlan>> roomTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int key = plan.getRoomId() * 1000 + plan.getTimeslotId(); // 简单的键生成方式
            roomTimeslotMap.computeIfAbsent(key, k -> new ArrayList<>()).add(plan);
        }

        //容量
        for (TeachingPlan plan : this.plans) {
            int roomCapacity = this.getRoom(plan.getRoomId()).getSize();
            int cohortSize = this.getCohort(plan.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                clashes++;
            }
        }

        //检查时间段和房间的冲突
        for (List<TeachingPlan> group : roomTimeslotMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                TeachingPlan planA = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    TeachingPlan planB = group.get(j);
                    if (planA.getPlanId() != planB.getPlanId()) {
                        clashes++;
                    }
                }
            }
        }

        //统计教授冲突
        Map<Integer, List<TeachingPlan>> professorTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int professorNum = this.getCourse(plan.getCourseId()).getProfessorNum();
            int timeslotId = plan.getTimeslotId();

            if (professorNum >= 1) {
                int key1 = plan.getProfessor1Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key1, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum >= 2) {
                int key2 = plan.getProfessor2Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key2, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum == 3) {
                int key3 = plan.getProfessor3Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key3, k -> new ArrayList<>()).add(plan);
            }
        }

        //检查教授时间段冲突
        for (List<TeachingPlan> group : professorTimeslotMap.values()) {
            if (group.size() > 1) {
                clashes += group.size() - 1;
            }
        }

        return clashes;
    }

    public int calcPenalty() {
        int penalty = 0;

        // 根据 roomId 和 timeslotId 分组
        Map<Integer, List<TeachingPlan>> roomTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int key = plan.getRoomId() * 1000 + plan.getTimeslotId(); // 简单的键生成方式
            roomTimeslotMap.computeIfAbsent(key, k -> new ArrayList<>()).add(plan);
        }

        // 容量硬约束
        for (TeachingPlan plan : this.plans) {
            int roomCapacity = this.getRoom(plan.getRoomId()).getSize();
            int cohortSize = this.getCohort(plan.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                penalty += 100;//惩罚值
            }
        }

        // 房间和时间段冲突硬约束
        for (List<TeachingPlan> group : roomTimeslotMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                TeachingPlan planA = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    TeachingPlan planB = group.get(j);
                    if (planA.getPlanId() != planB.getPlanId()) {
                        penalty += 100;//惩罚值
                    }
                }
            }
        }

        // 教授时间段冲突硬约束
        Map<Integer, List<TeachingPlan>> professorTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int professorNum = this.getCourse(plan.getCourseId()).getProfessorNum();
            int timeslotId = plan.getTimeslotId();

            if (professorNum >= 1) {
                int key1 = plan.getProfessor1Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key1, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum >= 2) {
                int key2 = plan.getProfessor2Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key2, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum == 3) {
                int key3 = plan.getProfessor3Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key3, k -> new ArrayList<>()).add(plan);
            }
        }

        // 检查教授时间段冲突
        for (List<TeachingPlan> group : professorTimeslotMap.values()) {
            if (group.size() > 1) {
                penalty += (group.size() - 1) * 100; // 增加冲突的惩罚值
            }
        }

        // 新增软约束：学生课程安排尽量相邻
        Map<Integer, List<TeachingPlan>> cohortCourseMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int cohortId = plan.getCohortId();
            cohortCourseMap.computeIfAbsent(cohortId, k -> new ArrayList<>()).add(plan);
        }

        for (List<TeachingPlan> plans : cohortCourseMap.values()) {
            plans.sort(Comparator.comparingInt(TeachingPlan::getTimeslotId)); // 按时间排序
            for (int i = 0; i < plans.size() - 1; i++) {
                TeachingPlan currentPlan = plans.get(i);
                TeachingPlan nextPlan = plans.get(i + 1);
                int timeslotDiff = nextPlan.getTimeslotId() - currentPlan.getTimeslotId();//暂时只这样设置
                if (timeslotDiff > 1) {
                    penalty += 1; // 时间段不相邻
                }
                if (currentPlan.getRoomId() != nextPlan.getRoomId()) {
                    penalty += 5; // 教室不同，增加小的惩罚值
                }
                if (!getValidProfessorIds(currentPlan).equals(getValidProfessorIds(nextPlan))) {
                    penalty += 5; // 教授不同，增加小的惩罚值
                }
            }
        }

        return penalty;
    }

    private Set<Integer> getValidProfessorIds(TeachingPlan plan) {
        Set<Integer> validProfessorIds = new HashSet<>();
        if (plan.getProfessor1Id() != 0) {
            validProfessorIds.add(plan.getProfessor1Id());
        }
        if (plan.getProfessor2Id() != 0) {
            validProfessorIds.add(plan.getProfessor2Id());
        }
        if (plan.getProfessor3Id() != 0) {
            validProfessorIds.add(plan.getProfessor3Id());
        }
        return validProfessorIds;
    }
}