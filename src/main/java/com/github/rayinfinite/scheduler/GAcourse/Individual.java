package com.github.rayinfinite.scheduler.GAcourse;

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
		int plansNum = timetable.getPlansNum();
		int chromosomeLength = plansNum * 5;

		for (Course course : timetable.getCourses().values()) {
			int duration = course.getDuration();
			chromosomeLength += (duration - 1) * 5;
		}

		int newChromosome[] = new int[chromosomeLength];
		int chromosomeIndex = 0;

		for (Cohort group : timetable.getCohortsAsArray()) {
			for (int courseId : group.getCourseIds()){

				int timeslotId = timetable.getRandomTimeslot().getTimeslotId();
				newChromosome[chromosomeIndex] = timeslotId;
				chromosomeIndex++;

				int roomId = timetable.getRandomRoom( ).getRoomId();
				newChromosome[chromosomeIndex] = roomId;
				chromosomeIndex++;

				Course course = timetable.getCourse(courseId);
				int professorNum = course.getProfessorNum();
				int[] professorIds = course.getProfessorIds();

				professorNum = Math.min(professorNum, professorIds.length);

				List<Integer> professorList = new ArrayList<>();
				for (int id : professorIds) {
					professorList.add(id);
				}

				Collections.shuffle(professorList);

				for (int i = 0; i < 3; i++) {
					if (i < professorNum) {
						newChromosome[chromosomeIndex] = professorList.get(i);
					} else {
						newChromosome[chromosomeIndex] = -1;
					}
					chromosomeIndex++;
				}
//				newChromosome[chromosomeIndex] = course.getRandomProfessorId();
//				chromosomeIndex++;
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
