package com.github.rayinfinite.scheduler.GAcourse;

public class InputData {
    private int courseId;
    private String practiceArea;
    private String courseName;
    private String courseCode;
    private int duration;
    private int cohortId;
    private String software;
    private Integer run;
    private String courseManager;
    private String gradCert;
    private int professorNum;
    private int teacherIds[];

    public InputData(int courseId, String practiceArea, String courseName, String courseCode, int duration, int cohortId, String software, int run, String courseManager, String gradCert, int teacherIds[], int professorNum) {
        this.courseId = courseId;
        this.practiceArea = practiceArea;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.teacherIds = teacherIds;
        this.software = software;
        this.courseManager = courseManager;
        this.gradCert = gradCert;
        this.professorNum = professorNum;
        this.duration = duration;
        this.run = run;
        this.cohortId = cohortId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public String getPracticeArea() {
        return this.practiceArea;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public String getSoftware() {
        return this.software;
    }

    public int getCohort() {
        return this.cohortId;
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

    public Integer getRun() {
        return this.run;
    }

    public int[] getTeacherIds() {
        return this.teacherIds;
    }

}
