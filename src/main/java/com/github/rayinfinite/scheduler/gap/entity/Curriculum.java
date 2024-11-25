package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Curriculum implements Serializable {
    private String classs;        // 班级
    private String course;        // 课程
    private String teacher;       // 授课教师
    private String classroom;     // 教室
    private int weekOrder;        // 星期几（表示一周的某天）
    private int courseOrder;      // 课程顺序（一天中的第几节课）
}
