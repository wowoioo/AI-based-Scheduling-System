package com.github.rayinfinite.scheduler.GAcourse.oldEntity;

public class Course {
    private int courseId;
    private String practiceArea;
    private String courseCode;
    private String course;
    private int professorIds[];
    private String software;
    private String courseManager;
    private String gradCert;
    private int professorNum;
    private int duration;
    private String run;

    public Course(int courseId, String practiceArea, String courseCode, String course, int professorIds[], String software, String courseManager, String gradCert, int professorNum, int duration, String run) {
        this.courseId = courseId;
        this.practiceArea = practiceArea;
        this.courseCode = courseCode;
        this.course = course;
        this.professorIds = professorIds;
        this.software = software;
        this.courseManager = courseManager;
        this.gradCert = gradCert;
        this.professorNum = professorNum;
        this.duration = duration;
        this.run = run;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public String getPracticeArea() {
        return this.practiceArea;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public String getCourseName() {
        return this.course;
    }

    public String getSoftware() {
        return this.software;
    }

    public String getCourseManager() {
        return this.courseManager;
    }

    public String getGradCert() {
        return this.gradCert;
    }

    public int getProfessorNum() {
        return professorNum;
    }

    public int getDuration() {
        return this.duration;
    }

    public int[] getProfessorIds() {
        return this.professorIds;
    }

//    public int getRandomProfessorId() {
//        int professorId = professorIds[(int) (professorIds.length * Math.random())];
//        return professorId;
//    }
}
