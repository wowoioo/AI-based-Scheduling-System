package com.github.rayinfinite.scheduler.GAcourse;

import com.github.rayinfinite.scheduler.GAcourse.config.GA;
import com.github.rayinfinite.scheduler.GAcourse.config.Population;
import com.github.rayinfinite.scheduler.entity.InputData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class TimatableGA {
	public static  int maxGenerations = 1000;

	public static Date convertToDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");  // 设置日期格式
		try {
			return sdf.parse(dateString);  // 返回 Date 对象
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Timetable initializeTimetable() {
		Timetable timetable = new Timetable();
		timetable.addRoom(0, "A0", 30);
		timetable.addRoom(1, "A1", 15);
		timetable.addRoom(2, "B1", 30);
		timetable.addRoom(3, "D11", 20);
		timetable.addRoom(4, "D12", 20);
		timetable.addRoom(5, "D13", 20);
		timetable.addRoom(6, "D14", 20);
		timetable.addRoom(7, "F10", 25);
		timetable.addRoom(8, "F11", 25);
		timetable.addRoom(9, "F12", 25);
		timetable.addRoom(10, "F13", 25);
		timetable.addRoom(11, "F14", 25);
		timetable.addRoom(12, "F15", 25);
		timetable.addRoom(13, "F16", 25);
		timetable.addRoom(14, "F17", 25);
		timetable.addRoom(15, "F18", 25);

		timetable.addTimeslot(1, convertToDate("2022/2/7"));
		timetable.addTimeslot(2, convertToDate("2022/2/8"));
		timetable.addTimeslot(3, convertToDate("2022/2/9"));
		timetable.addTimeslot(4, convertToDate("2022/2/10"));
		timetable.addTimeslot(5, convertToDate("2022/2/11"));
		timetable.addTimeslot(6, convertToDate("2022/2/12"));
		timetable.addTimeslot(7, convertToDate("2022/2/13"));
		timetable.addTimeslot(8, convertToDate("2022/2/14"));
		timetable.addTimeslot(9, convertToDate("2022/2/15"));
		timetable.addTimeslot(10, convertToDate("2022/2/16"));
		timetable.addTimeslot(11, convertToDate("2022/2/17"));
		timetable.addTimeslot(12, convertToDate("2022/2/18"));
		timetable.addTimeslot(13, convertToDate("2022/2/19"));
		timetable.addTimeslot(14, convertToDate("2022/2/20"));
		timetable.addTimeslot(15, convertToDate("2022/2/21"));

		timetable.addProfessor(-1, " - ");
		timetable.addProfessor(1, "Lecturer1");
		timetable.addProfessor(2, "Lecturer2");
		timetable.addProfessor(3, "Lecturer3");
		timetable.addProfessor(4, "Lecturer4");
		timetable.addProfessor(5, "Lecturer5");
		timetable.addProfessor(6, "Lecturer6");
		timetable.addProfessor(7, "Lecturer7");
		timetable.addProfessor(8, "Lecturer8");
		timetable.addProfessor(9, "Lecturer9");

		timetable.addCourse(new InputData(1, "PA2", "Course1", "CC2", 5, 1, "-", 1, "Lecturer1", "Grad Cert 1", new int[]{1, 2, 3, 4, 5}, 1));
		timetable.addCourse(new InputData(2, "PA2", "Course2", "CC3", 1, 1, "-", 3, "Lecturer1", "Grad Cert 1", new int[]{1, 3, 4, 5, 7, 8, 9}, 1));
		timetable.addCourse(new InputData(3, "PA2", "Course3", "CC4", 2, 2, "-", 1, "Lecturer1", "Grad Cert 1", new int[]{1, 2, 6, 8, 9}, 2));
		timetable.addCourse(new InputData(4, "PA2", "Course4", "CC5", 1, 3, "-", 2, "Lecturer1", "Grad Cert 1", new int[]{3, 4, 5, 6}, 1));
		timetable.addCourse(new InputData(5, "PA2", "Course5", "CC6", 3, 4, "-", 1, "Lecturer1", "Grad Cert 1", new int[]{1, 2, 3}, 1));
		timetable.addCourse(new InputData(6, "PA2", "Course6", "CC7", 1, 5, "-", 2, "Lecturer1", "Grad Cert 1", new int[]{1, 4, 7, 8}, 1));

		timetable.addCohort(1, "Cohort1", 10, 1, "1");
		timetable.addCohort(2, "Cohort2",  30, 2, "1");
		timetable.addCohort(3, "Cohort3",  18, 2, "1");
		timetable.addCohort(4, "Cohort4",  25, 3, "1");
		timetable.addCohort(5, "Cohort5",  20, 4, "1");
		timetable.addCohort(6, "Cohort6",  22, 5, "1");
		timetable.addCohort(7, "Cohort7",  16, 5, "1");
		timetable.addCohort(8, "Cohort8",  18, 6, "1");
		timetable.addCohort(9, "Cohort9",  24, 7, "1");
		timetable.addCohort(10, "Cohort10",  25, 8, "1");

		return timetable;
	}
	public static void main(String[] args) {
		Timetable timetable = initializeTimetable();

		GA ga = new GA(100, 0.001, 0.98, 1, 5);

		Population population = ga.initPopulation(timetable);

		int generation = 1;

		while(ga.isTerminationConditionMet1(population) == false && ga.isTerminationconditionMet2(generation, maxGenerations) == false) {
			System.out.println("G" + generation + "Best fitness:" + population.getFittest(0).getFitness());

			//交叉
			population = ga.crossoverPopulation(population);

			//变异
			population = ga.mutatePopulation(population, timetable);

			ga.evalPopulation(population, timetable);

			generation++;
		}

		timetable.createPlans(population.getFittest(0));
		System.out.println("Solution found in " + generation + " generations");
		System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
		System.out.println("Clashes: " + timetable.calcClashes());

		List<InputData> inputDataList = new ArrayList<>();
		TeachingPlan[] plans = timetable.getPlans();

		// 生成 List<InputData>
		for (TeachingPlan bestPlan : plans) {
			int courseId = bestPlan.getCourseId();
			String practiceArea = timetable.getCourse(courseId).getPracticeArea();
			String courseName = timetable.getCourse(courseId).getCourseName();
			String courseCode = timetable.getCourse(courseId).getCourseCode();
			int duration = timetable.getCourse(courseId).getDuration();
			int cohortId = bestPlan.getCohortId();
			String software = timetable.getCourse(courseId).getSoftware();
			int run = timetable.getCourse(courseId).getRun();
			String courseManager = timetable.getCourse(courseId).getCourseManager();
			String gradCert = timetable.getCourse(courseId).getGradCert();
			String cohort = timetable.getCohort(cohortId).getCohort();
			String classroom = timetable.getRoom(bestPlan.getRoomId()).getRoomNumber();

			// 获取时间信息
			Date timeslotDate = timetable.getTimeslot(bestPlan.getTimeslotId()).getTimeslot();
			String time = TimetableOutput.convertDateToString(timeslotDate);
			// 获取教师 ID 列表
			int professorNum = timetable.getCourse(courseId).getProfessorNum();

			int[] teacherIds = new int[professorNum];
			teacherIds[0] = bestPlan.getProfessor1Id();
			if (professorNum > 1) {
				teacherIds[1] = bestPlan.getProfessor2Id();
			}
			if (professorNum > 2) {
				teacherIds[2] = bestPlan.getProfessor3Id();
			}

			// 获取教师名称
			String teacher1 = teacherIds.length > 0 && teacherIds[0] != -1
					? timetable.getProfessor(teacherIds[0]).getProfessorName()
					: null;
			String teacher2 = teacherIds.length > 1 && teacherIds[1] != -1
					? timetable.getProfessor(teacherIds[1]).getProfessorName()
					: null;
			String teacher3 = teacherIds.length > 2 && teacherIds[2] != -1
					? timetable.getProfessor(teacherIds[2]).getProfessorName()
					: null;

			// 构建 InputData 对象
			InputData inputData = new InputData(
					courseId, practiceArea, courseName, courseCode, duration, cohortId,
					software, run, courseManager, gradCert, teacherIds, professorNum
			);

			// 设置教师名称
			inputData.setTeacher1(teacher1);
			inputData.setTeacher2(teacher2);
			inputData.setTeacher3(teacher3);
			inputData.setClassroom(classroom);
			inputData.setCourseDate(timeslotDate);
//			inputData.setWeek(time);
			inputDataList.add(inputData);
			inputData.setCohort(cohort);
		}

		// 打印所有 InputData
		IntStream.range(0, inputDataList.size()).forEach(i -> inputDataList.get(i).setId(i + 1));
		inputDataList.forEach(System.out::println);
	}

	// 转换日期为字符串
	private static String convertDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(date);
	}

}
