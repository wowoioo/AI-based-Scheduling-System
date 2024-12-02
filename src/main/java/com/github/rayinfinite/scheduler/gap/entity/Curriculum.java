package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Curriculum implements Serializable {
    private String classs;        // 班级
    private String course;        // 课程
    private String teacher;       // 授课教师
    private String classroom;     // 教室
    private int weekOrder;        // 星期几（表示一周的某天）
    private int courseOrder;      // 课程顺序（一天中的第几节课）

//    private String courseName;    // 课程名称
//    private String courseCode;    // 课程代码
//    private Integer duration;     // 课程时长
//
//    private String software;      // 使用的软件
//
//    private String cohort;        // 班级（如全日制、非全日制）
//    private String run;           // 课程运行批次
//
//    private Date courseDate;      // 课程日期
//    private String week;          // 星期几
//    private String classroom;     // 教室编号
//
//    private String teacher1;      // 授课教师1
//    private String teacher2;      // 授课教师2
//    private String teacher3;      // 授课教师3
//
//    private String manager;       // 课程管理员
//    private String cert;          // 证书类型
}
