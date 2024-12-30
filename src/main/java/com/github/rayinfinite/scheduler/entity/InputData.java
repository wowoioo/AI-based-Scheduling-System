package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.github.rayinfinite.scheduler.excel.IntegerConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@ToString
//@Table(name = "input_data")
public class InputData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
    @Setter
    Integer id;
    String practiceArea;
    String courseName;
    String courseCode;
    @ExcelProperty(converter = IntegerConverter.class)
    Integer duration;
    String software;
    String cohort;
    @ExcelIgnore
    Integer cohortId;
    Integer run;
    @ExcelIgnore
    Date courseDate;
    @ExcelIgnore
    String week;
    @ExcelIgnore
    String classroom;
    String teacher1;
    String teacher2;
    String teacher3;
    String manager;
    String cert;
    Integer professorNum;
    @ExcelIgnore
    @Transient
    int[] teacherIds;

    public InputData(Integer courseId, String practiceArea, String courseName, String courseCode, int duration, int cohortId, String software, int run, String courseManager, String gradCert, int teacherIds[], int professorNum) {
        this.id = courseId;
        this.practiceArea = practiceArea;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.teacherIds = teacherIds;
        this.software = software;
        this.manager = courseManager;
        this.cert = gradCert;
        this.professorNum = professorNum;
        this.duration = duration;
        this.run = run;
        this.cohortId = cohortId;
    }

    public InputData() {

    }

    public int getCourseId() {
        return this.id;
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

    public int getCohortId() {
        return this.cohortId;
    }

    public String getCourseManager() {
        return this.manager;
    }

    public String getGradCert() {
        return this.cert;
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
