package com.github.rayinfinite.scheduler.gap.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Teacher1 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
    private java.lang.String id;

    private String teacher1;
    private List<String> enableCourse;
    private List<Date> unavailableDate;

}
