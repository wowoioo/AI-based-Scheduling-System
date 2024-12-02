package com.github.rayinfinite.scheduler.gap.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

public class Teacher implements Serializable {
    private String teacher;
    private List<String> enableCourse;
    private List<Date> unavailableDate;

}
