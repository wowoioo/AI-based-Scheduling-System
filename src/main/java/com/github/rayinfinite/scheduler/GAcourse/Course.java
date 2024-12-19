package com.github.rayinfinite.scheduler.GAcourse;

public class Course {
    private final int courseId;
    private final String practiceArea;
    private final String courseCode;
    private final String course;
    private final int professorIds[];
    private final String software;
    private final String courseManager;
    private final String gradCert;
    private final int professorNum;
    private final int duration;
    private final String run;

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
