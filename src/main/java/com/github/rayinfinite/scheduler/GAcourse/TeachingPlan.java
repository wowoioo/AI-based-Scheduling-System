package com.github.rayinfinite.scheduler.GAcourse;

public class TeachingPlan {
    private final int planId;
    private final int cohortId;
    private final int courseId;
    private int professor1Id;
    private int professor2Id;
    private int professor3Id;
    private int timeslotId;
    private int roomId;

    public TeachingPlan(int planId, int cohortId, int courseId) {
        this.planId = planId;
        this.courseId = courseId;
        this.cohortId = cohortId;
    }

    public void addProfessor1(int professorId) {
        this.professor1Id = professorId;
    }

    public void addProfessor2(int professorId) {
        this.professor2Id = professorId;
    }

    public void addProfessor3(int professorId) {
        this.professor3Id = professorId;
    }

    public void addTimeslot(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getPlanId() {
        return this.planId;
    }

    public int getCohortId() {
        return this.cohortId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public int getProfessor1Id() {
        return this.professor1Id;
    }

    public int getProfessor2Id() {
        return this.professor2Id;
    }


    public int getProfessor3Id() {
        return this.professor3Id;
    }


    public int getTimeslotId() {
        return this.timeslotId;
    }

    public int getRoomId() {
        return this.roomId;
    }
}
