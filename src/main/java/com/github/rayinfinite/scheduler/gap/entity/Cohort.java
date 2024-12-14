package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Cohort implements Serializable {
    private String type; // "FULL_TIME" 或 "PART_TIME"
    private String major; // "SE", "IS", "EBAC" 等
    private int year; // 1, 2, 3 等
    private String courseName;// 需要跟course关联吗？

}
