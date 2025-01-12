package com.github.rayinfinite.scheduler.GAcourse;

import com.github.rayinfinite.scheduler.GAcourse.config.Individual;
import com.github.rayinfinite.scheduler.entity.Professor;
import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.Cohort;
import com.github.rayinfinite.scheduler.entity.Timeslot;

import java.util.*;

public class Timetable {
    private final HashMap<Integer, Classroom> rooms;
    private final HashMap<Integer, InputData> courses;
    private final HashMap<Integer, Cohort> cohorts;
    private final HashMap<Integer, Timeslot> timeslots;
    private final HashMap<Integer, Professor> professors;

    private TeachingPlan plans[];

    private int plansNum = 0;

    public Timetable() {
        this.rooms = new HashMap<>();
        this.courses = new HashMap<>();
        this.cohorts = new HashMap<>();
        this.timeslots = new HashMap<>();
        this.professors = new HashMap<>();
    }

    //浅拷贝，用于适应度计算
    public Timetable(Timetable cloneable) {
        this.rooms = cloneable.getRooms();
        this.courses = cloneable.getCourses();
        this.cohorts = cloneable.getCohorts();
        this.timeslots = cloneable.getTimeslots();
        this.professors = cloneable.getProfessors();
    }

    public HashMap<Integer, InputData> getCourses() {
        return this.courses;
    }

    public HashMap<Integer, Professor> getProfessors() {
        return professors;
    }

    public HashMap<Integer, Cohort> getCohorts() {
        return this.cohorts;
    }

    public HashMap<Integer, Timeslot> getTimeslots() {
        return this.timeslots;
    }

    public void addRoom(int roomId, String roomName, int capacity) {
        this.rooms.put(roomId, new Classroom(roomId, roomName, capacity));
    }

    public void addProfessor(int professorId, String professorName) {
        this.professors.put(professorId, new Professor(professorId, professorName));
    }

    public void addCourse(InputData inputData) {
        this.courses.put(inputData.getCourseId(), inputData);
    }

    public void addCohort(int cohortId, String cohort, int cohortSize,int typeId, String cohortType, int courseIds[]) {
        this.cohorts.put(cohortId, new Cohort(cohortId, cohort, cohortSize, typeId, cohortType, courseIds));
        this.plansNum = 0;
    }

    public void addTimeslot(int timeslotId, Date timeslot) {
        this.timeslots.put(timeslotId, new Timeslot(timeslotId, timeslot));
    }

    public void createPlans(Individual individual) {
        int totalPlans = 0;
        for (Cohort cohort : this.getCohortsAsArray()) {
            int courseIds[] = cohort.getCourseIds();
            for (int courseId : courseIds) {
                InputData course = this.getCourse(courseId);
                totalPlans += course.getDuration();  // 还没有加run#，后面也要加在这
            }
        }
        TeachingPlan plans[] = new TeachingPlan[totalPlans];


        int chromosome[] = individual.getChromosome();
        int chromosomePos = 0;
        int planIndex = 0;

        for (Cohort cohort : this.getCohortsAsArray()) {
            int courseIds[] = cohort.getCourseIds();
            for (int courseId : courseIds) {
                InputData course = this.getCourse(courseId);
                int duration = course.getDuration(); // 获取课程持续时间

                for (int dayOffset = 0; dayOffset < duration; dayOffset++) {
                    plans[planIndex] = new TeachingPlan(planIndex, cohort.getCohortId(), courseId);


                    int timeslotId = chromosome[chromosomePos] + dayOffset;
                    if (timeslotId > this.getMaxTimeslotId()) {
                        timeslotId = chromosome[chromosomePos];
                    }
                    plans[planIndex].addTimeslot(timeslotId);

                    // 对于持续时间内的计划，保持教室一致
                    if (dayOffset == 0) {
                        plans[planIndex].setRoomId(chromosome[chromosomePos + 1]); // 仅在第一个时间段设置教室
                    } else {
                        plans[planIndex].setRoomId(plans[planIndex - 1].getRoomId()); // 延续上一个计划的教室
                    }

                    // 对于持续时间内的计划，保持教授一致
                    if (dayOffset == 0) {
                        plans[planIndex].addProfessor1(chromosome[chromosomePos + 2]);
                        plans[planIndex].addProfessor2(chromosome[chromosomePos + 3]);
                        plans[planIndex].addProfessor3(chromosome[chromosomePos + 4]);
                    } else {
                        plans[planIndex].addProfessor1(plans[planIndex - 1].getProfessor1Id());
                        plans[planIndex].addProfessor2(plans[planIndex - 1].getProfessor2Id());
                        plans[planIndex].addProfessor3(plans[planIndex - 1].getProfessor3Id());
                    }

                    planIndex++;
                }
                chromosomePos += 5; // 每个课程占用 5 个基因
            }
        }
        this.plans = plans;
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
            System.out.println("Rooms doesn't contain key " + roomId);
        }
        return this.rooms.get(roomId);
    }

    public HashMap<Integer, Classroom> getRooms() {
        return this.rooms;
    }

    public Classroom getRandomRoom() {
        Object[] roomsArray = this.rooms.values().toArray( );
        Classroom room = (Classroom) roomsArray[(int) (roomsArray.length * Math.random())];
        return room;
    }

    public InputData getCourse(int courseId) {
        return this.courses.get(courseId);
    }

    public Professor getProfessor(int professorId) {
        return this.professors.get(professorId);
    }

    public int[] getCohortCourses(int cohortId) {
        Cohort cohort = this.cohorts.get(cohortId);
        return cohort.getCourseIds();
    }

    public Cohort getCohort(int cohortId) {
        return (Cohort) this.cohorts.get(cohortId);
    }

    public Cohort[] getCohortsAsArray() {
        return this.cohorts.values().toArray(new Cohort[this.cohorts.size()]);
    }

    public Timeslot getTimeslot(int timeslotId) {
        return (Timeslot) this.timeslots.get(timeslotId);
    }

    public Timeslot getRandomTimeslot() {
        Object[] timeslotArray = this.timeslots.values().toArray();
        Timeslot timeslot = (Timeslot) timeslotArray[(int) (timeslotArray.length * Math.random())];
        return timeslot;
    }

    public TeachingPlan[] getPlans() {
        return this.plans;
    }

    public int getPlansNum() {
        if (this.plansNum > 0){
            return this.plansNum;
        }

        int plansNum = 0;
        Cohort cohorts[] = (Cohort[]) this.cohorts.values().toArray(new Cohort[this.cohorts.size()]);

        for (Cohort cohort : cohorts) {
            plansNum += cohort.getCourseIds().length;
        }
        this.plansNum = plansNum;

        return this.plansNum;
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
            int roomCapacity = this.getRoom(plan.getRoomId()).getRoomCapacity();
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
            int roomCapacity = this.getRoom(plan.getRoomId()).getRoomCapacity();
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
            plans.sort((a, b) -> Integer.compare(a.getTimeslotId(), b.getTimeslotId())); // 按时间排序
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