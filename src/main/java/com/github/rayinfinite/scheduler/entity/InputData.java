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
    Long id;
    String practiceArea;
    String courseName;
    String courseCode;
    @ExcelProperty(converter = IntegerConverter.class)
    Integer duration;
    String software;
    String cohort;
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
    @ExcelIgnore
    @Transient
    int[] teacherIds;
}
