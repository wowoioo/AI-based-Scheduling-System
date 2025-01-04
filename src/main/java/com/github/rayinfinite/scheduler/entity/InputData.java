package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.github.rayinfinite.scheduler.excel.DateConverter;
import com.github.rayinfinite.scheduler.excel.IntegerConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@ToString
@NoArgsConstructor
public class InputData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
//    @Setter
    Integer id;
    String practiceArea;
    String courseName;
    String courseCode;
    @ExcelProperty(converter = IntegerConverter.class)
    Integer duration;
    String software;
    //    @ExcelIgnore
    @ExcelProperty(converter = IntegerConverter.class)
    Integer cohortId;
    String cohort;
    @ExcelProperty(converter = IntegerConverter.class)
    Integer run;
    @ExcelIgnore
    @ExcelProperty(converter = DateConverter.class)
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
    @ExcelProperty(converter = IntegerConverter.class)
    Integer professorNum;

    @ExcelIgnore
    @Transient
    int[] teacherIds;

    public InputData(Integer courseId, String practiceArea, String courseName, String courseCode, Integer duration, int cohortId, String software, int run, String courseManager, String gradCert, int teacherIds[], int professorNum) {
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

    public int getCourseId() {
        return this.id;
    }

    public String getCourseManager() {
        return this.manager;
    }

    public String getGradCert() {
        return this.cert;
    }

//    public int getProfessorNum() {
//        return professorNum != null ? professorNum : 1;
//    }
}
