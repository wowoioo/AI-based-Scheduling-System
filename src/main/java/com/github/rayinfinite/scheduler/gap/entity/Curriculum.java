package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Curriculum implements Serializable {
    private String cohort;
    private String courseCode;
    private String courseName;

    private String teacher1;
    private String teacher2;
    private String teacher3;

    private Long classId;

    private Integer weekOrder;
    private Date courseDate;
    private Integer duration;

    private String cohortType;
    private String cohortMajor;
    private String cohortYear;

}
