package com.github.rayinfinite.scheduler.GAcourse;

import com.github.rayinfinite.scheduler.GAcourse.config.GA;
import com.github.rayinfinite.scheduler.GAcourse.config.Population;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

		timetable.addCohort(1, "Cohort1", 10, 1, "1", new int[]{1, 3, 4});
		timetable.addCohort(2, "Cohort2",  30, 2, "1", new int[]{2, 3, 5, 6});
		timetable.addCohort(3, "Cohort3",  18, 2, "1", new int[]{3, 4, 5});
		timetable.addCohort(4, "Cohort4",  25, 3, "1", new int[]{1, 4});
		timetable.addCohort(5, "Cohort5",  20, 4, "1", new int[]{2, 3, 5});
		timetable.addCohort(6, "Cohort6",  22, 5, "1", new int[]{1, 4, 5});
		timetable.addCohort(7, "Cohort7",  16, 5, "1", new int[]{1, 3});
		timetable.addCohort(8, "Cohort8",  18, 6, "1", new int[]{2, 6});
		timetable.addCohort(9, "Cohort9",  24, 7, "1", new int[]{1, 6});
		timetable.addCohort(10, "Cohort10",  25, 8, "1", new int[]{3, 4});

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

		TimetableOutput timetableOutput = new TimetableOutput();
		List<TimetableOutput.InputData> timetableList = timetableOutput.generateTimetableList(timetable, population, generation);

        // 打印列表
		for (TimetableOutput.InputData data : timetableList) {
			System.out.println(data);
		}
	}
}
