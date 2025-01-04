package com.github.rayinfinite.scheduler.GAcourse.config;

import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.GAcourse.Timetable;
import com.github.rayinfinite.scheduler.entity.Cohort;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Individual {
	private int[] chromosome;
	private double fitness = -1;

	public Individual(int[] chromosome) {
		this.chromosome = chromosome;
	}

	public Individual(int chromosomeLength) {
		this.chromosome = new int[chromosomeLength];
		for(int gene = 0; gene < chromosomeLength; gene++) {
			if(0.5 < Math.random()) {
				this.setGene(gene, 1);
			} else {
				this.setGene(gene, 0);
			}
		}
	}

	public Individual(Timetable timetable) {
		int plansNum = timetable.getPlansNum(timetable);
		int chromosomeLength = plansNum * 5;

		for (InputData course : timetable.getCourses().values()) {
			int run = course.getRun();
			int duration = course.getDuration();
			int length1 = (duration - 1) * 5;
			chromosomeLength += length1 * 5;
//			chromosomeLength += (run - 1) * length1;
		}

		int newChromosome[] = new int[chromosomeLength];
		int chromosomeIndex = 0;

		for (InputData course : timetable.getCourses().values()) { // 获取 Timetable 中所有课程
			// 随机分配时间段
			int timeslotId = timetable.getRandomTimeslot().getTimeslotId();
			newChromosome[chromosomeIndex] = timeslotId;
			chromosomeIndex++;

			// 随机分配教室
			int roomId = timetable.getRandomRoom().getRoomId();
			newChromosome[chromosomeIndex] = roomId;
			chromosomeIndex++;

			// 获取教授信息
			int professorNum = course.getProfessorNum();
			int[] professorIds = course.getTeacherIds();

			professorNum = Math.min(professorNum, professorIds.length);

			List<Integer> professorList = new ArrayList<>();
			for (int id : professorIds) {
				professorList.add(id);
			}

			// 打乱教授列表顺序
			Collections.shuffle(professorList);

			// 为课程分配教授
			for (int i = 0; i < 3; i++) {
				if (i < professorNum) {
					newChromosome[chromosomeIndex] = professorList.get(i);
				} else {
					newChromosome[chromosomeIndex] = -1; // 如果没有足够的教授，设置为 -1
				}
				chromosomeIndex++;
			}
		}
		this.chromosome = newChromosome;
	}

	public int[] getChromosome() {
		return  this.chromosome;
	}

	public int getChromosomeLength() {
		return  this.chromosome.length;
	}

	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}

	public int getGene(int offset) {
		return  this.chromosome[offset];
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return this.fitness;
	}

	@Override
	public String toString() {
		String output = "";
		for(int gene = 0; gene < this.chromosome.length; gene++) {
			output += this.chromosome[gene];
		}
		return output;
	}
}
