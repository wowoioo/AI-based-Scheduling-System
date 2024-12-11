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

//    private Integer classSize;
//    private String software;

    private Integer weekOrder;
    private Date courseDate;
    private Integer duration;


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
