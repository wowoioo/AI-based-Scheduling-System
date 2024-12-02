package com.github.rayinfinite.scheduler.gap.util;

import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.gap.entity.Classroom;
import com.github.rayinfinite.scheduler.gap.entity.TeachingPlan;
import com.github.rayinfinite.scheduler.gap.entity.Time;
import com.github.rayinfinite.scheduler.gap.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    // 分隔符定义
    public static final String TOKEN_SEPARATOR = ":";
    public static final String GENE_DELIMITER = "+";

    public static List<TeachingPlan> PLAN_LIST = new ArrayList<>();
    public static List<Classroom> CLASSROOM_LIST = new ArrayList<>();
    public static List<Teacher> TEACHER_LIST = new ArrayList<>();
    public static List<Time> TIME_LIST = new ArrayList<>();

    public static final int COURSE_NAME = 0;    // 课程名称
    public static final int COURSE_CODE = 1;    // 课程代码
    public static final int DURATION = 2;       // 课程时长
    public static final int SOFTWARE = 3;       // 使用软件
    public static final int COHORT = 4;         // 班级（如全日制、非全日制）
    public static final int RUN = 5;            // 课程运行批次
    public static final int COURSE_DATE = 6;    // 课程日期
    public static final int WEEK = 7;           // 星期几
    public static final int CLASSROOM = 8;      // 教室编号
    public static final int TEACHER1 = 9;       // 授课教师1
    public static final int TEACHER2 = 10;      // 授课教师2
    public static final int TEACHER3 = 11;      // 授课教师3
    public static final int MANAGER = 12;       // 课程经理
    public static final int CERT = 13;          // 证书类型

    // 参数范围定义（根据业务需求调整）
    public static final int MAX_DURATION = 10;  // 课程时长最大值
    public static final int MAX_CLASSROOM = 100; // 教室编号最大值

    // Actual Data
    public static List<InputData> INPUT_DATA_LIST = new ArrayList<>(); // History data from Excel file

    // GA Parameters
    public static final int POPULATION_SIZE = 50;
    public static final int MAX_GENERATIONS = 1000;
    public static final double MUTATION_RATE = 0.05;
    public static final double CROSSOVER_RATE = 0.8;
    public static final double FITNESS_THRESHOLD = 0.025;


}
