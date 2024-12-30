package com.github.rayinfinite.scheduler.GAcourse.oldEntity;

public class Professor {
    private int professorId;
    private String professorName;
//    private String unavailableDate;

    public Professor(int professorId, String professorName) {
        this.professorId = professorId;
        this.professorName = professorName;
    }

    public int getProfessorId() {
        return this.professorId;
    }

    public String getProfessorName() {
        return this.professorName;
    }
}
