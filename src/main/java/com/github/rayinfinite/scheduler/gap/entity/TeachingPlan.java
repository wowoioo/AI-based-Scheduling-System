package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class TeachingPlan implements Serializable {
    private String courseName;    // 课程名称
    private String courseCode;    // 课程代码
    private Integer duration;     // 课程时长

    private String software;      // 使用的软件

    private String cohort;        // 班级
    private String run;           // 课程运行批次

    private Date courseDate;      // 课程日期
    private String week;          // 星期几
    private Long classroom;     // 教室编号

    private String teacher1;      // 授课教师1
    private String teacher2;      // 授课教师2
    private String teacher3;      // 授课教师3

    private String manager;       // 课程管理员
    private String cert;          // 证书类型

    private int classSize;       // 班级大小（学生人数）


    /*private String firstTime;     // 第一次上课的时间
    private String secondTime;    // 第二次上课的时间
    private String thirdTime;     // 第三次上课的时间
    private String fourthTime;    // 第四次上课的时间*/
}
