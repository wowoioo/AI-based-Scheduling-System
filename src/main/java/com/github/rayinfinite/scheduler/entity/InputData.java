package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.github.rayinfinite.scheduler.excel.DateConverter;
import com.github.rayinfinite.scheduler.excel.IntegerConverter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
//@Table(name = "input_data")
public class InputData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
    Long id;
    String practiceArea;
    String courseName;
    String courseCode;
    @ExcelProperty(converter = IntegerConverter.class)
     Integer duration;
    String software;
    String cohort;
    String run;
    @ExcelProperty(converter = DateConverter.class)
    Date courseDate;
    String week;
    String classroom;
    String teacher1;
    String teacher2;
    String teacher3;
    String manager;
    String cert;

    String cohortType;
    String cohortMajor;
    String cohortYear;
}
