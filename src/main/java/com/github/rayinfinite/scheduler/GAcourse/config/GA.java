package com.github.rayinfinite.scheduler.GAcourse.config;

import com.github.rayinfinite.scheduler.GAcourse.Timetable;
import com.github.rayinfinite.scheduler.GAcourse.config.Individual;
import com.github.rayinfinite.scheduler.GAcourse.config.Population;

import java.util.Random;

public class GA {
	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	protected int tournamentSize;
	private int penalty;

	public Population initPopulation(Timetable timetable) {
		Population population = new Population(this.populationSize, timetable);
		return population;
	}

	public double calcFitness(Individual individual, Timetable timetable) {
		Timetable threadTimetable = new Timetable(timetable);
		threadTimetable.createPlans(individual);

		double penalty = threadTimetable.calcPenalty();

		double fitness = 1 / (penalty + 1);
		individual.setFitness(fitness);

		return fitness;
	}

	public void evalPopulation(Population population, Timetable timetable) {
		double populationFitness = 0;
		for (Individual individual : population.getIndividuals()) {
			populationFitness += this.calcFitness(individual, timetable);
		}

		population.setPopulationFitness(populationFitness);
	}

	public boolean isTerminationConditionMet1(Population population) {
		return population.getFittest(0).getFitness() == 1.0;
	}

	public boolean isTerminationconditionMet2(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	//锦标赛选父母
	public Individual selectParent(Population population) {
		Population tournament = new Population(this.tournamentSize);
		Random random = new Random();

		// 随机选择 tournamentSize 个个体放入锦标赛种群
		for (int i = 0; i < this.tournamentSize; i++) {
			int randomIndex = random.nextInt(population.size());
			Individual tournamentIndividual = population.getIndividual(randomIndex);
			tournament.setIndividual(i, tournamentIndividual);
		}

		return tournament.getFittest(0);
	}

//	//均匀交叉
//	public Population crossoverPopulation(Population population) {
//		Population newPopulation = new Population(population.size());
//		for(int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
//			Individual parent1 = population.getFittest(populationIndex);
//			if(this.crossoverRate > Math.random() && populationIndex > this.elitismCount) {
//				Individual offspring = new Individual(parent1.getChromosomeLength());
//				Individual parent2 = selectParent((population));
//				for(int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
//					if(0.5 > Math.random()) {
//						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
//					} else {
//						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
//					}
//				}
//				newPopulation.setIndividual(populationIndex, offspring);
//			} else {
//				newPopulation.setIndividual(populationIndex, parent1);
//			}
//		}
//		return newPopulation;
//	}

	public Population crossoverPopulation(Population population) {
		Population newPopulation = new Population(population.size());

		for (int populationIndex = 0; populationIndex <population.size(); populationIndex++){
			Individual parent1 = population.getFittest(populationIndex);

			if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {

				Individual offspring = new Individual(parent1.getChromosomeLength());

				Individual parent2 = this.selectParent(population);

				int swapPoint = (int)(Math.random() *(parent1.getChromosomeLength() + 1));

				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					if (geneIndex < swapPoint){
						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
					}else {
						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
					}
				}
				newPopulation.setIndividual(populationIndex,offspring);
			}else {
				newPopulation. setIndividual(populationIndex, parent1);
			}
		}
		return newPopulation;
	}




	public Population mutatePopulation(Population population, Timetable timetable) {
		Population newPopulation = new Population(this.populationSize);
		for(int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);

			Individual randomIndividual = new Individual(timetable);

			for(int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
				if (populationIndex > this.elitismCount) {
					if(this.mutationRate > Math.random()) {
						individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
					}
				}
			}
			newPopulation.setIndividual(populationIndex, individual);
		}
		return newPopulation;
	}

	public GA(int populationSize, double mutationRate, double crossoverRate,int elitismCount, int tournamentSize) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

}
