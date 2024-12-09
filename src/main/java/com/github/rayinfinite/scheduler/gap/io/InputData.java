package com.github.rayinfinite.scheduler.gap.io;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.gap.entity.Teacher;
import com.github.rayinfinite.scheduler.gap.entity.TeachingPlan;
import com.github.rayinfinite.scheduler.gap.entity.Time;
import com.github.rayinfinite.scheduler.gap.util.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yi
 */
public class InputData {

    public static void read() {
        // 手动初始化 TeachingPlan 数据
        List<TeachingPlan> planList = new ArrayList<>();
        TeachingPlan plan1 = new TeachingPlan();
        plan1.setCourseName("Course1");
        plan1.setCourseCode("CC1");
        plan1.setDuration(1);
        plan1.setSoftware("-");
        plan1.setCohort("Cohort1");
        plan1.setRun("1");
        plan1.setCourseDate(new Date());
        plan1.setWeek(1);
        plan1.setClassroom("Room1");
        plan1.setTeacher1("TBC");
        plan1.setTeacher2(" ");
        plan1.setTeacher3(" ");
        plan1.setManager("-");
        plan1.setCert("-");
        plan1.setClassSize(30);
        planList.add(plan1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.PLAN_LIST = planList;

        // 手动初始化 Classroom 数据
        List<Classroom> classroomList = new ArrayList<>();
        Classroom classroom1 = new Classroom();
        classroom1.setId(1L);
        classroom1.setName("Room1");
        classroom1.setSize(30);
        classroom1.setSoftware("-");
        classroomList.add(classroom1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.CLASSROOM_LIST = classroomList;

        // 手动初始化 Teacher 数据
        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1.setId("T1");
        teacher1.setTeacher("TBC");
        teacher1.setEnableCourse(List.of("course1"));
        teacher1.setUnavailableDate((List<Date>) new Date(1702108800000L));
        teacherList.add(teacher1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.TEACHER_LIST = teacherList;

        // 手动初始化 Time 数据
        List<Time> timeList = new ArrayList<>();
        Time time1 = new Time();
        time1.setDuration(1);
        time1.setWeekOrder(1);
        timeList.add(time1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.TIME_LIST = timeList;
    }
}
