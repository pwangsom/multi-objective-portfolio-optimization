package com.amontep.portfolio;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import com.amontep.portfolio.problem.TwoPorfolioDoubleConstrainedProblem;

public class NsgaiiDoubleProblemRunner extends AbstractAlgorithmRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Problem<DoubleSolution> problem;
		Algorithm<List<DoubleSolution>> algorithm;
		CrossoverOperator<DoubleSolution> crossover;
		MutationOperator<DoubleSolution> mutation;
		SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

		problem = new TwoPorfolioDoubleConstrainedProblem();

		double crossoverProbability = 0.9;
		double crossoverDistributionIndex = 20.0;
		crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<DoubleSolution>(
				new RankingAndCrowdingDistanceComparator<DoubleSolution>());

		algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation).setSelectionOperator(selection)
				.setMaxEvaluations(900000).setPopulationSize(100).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<DoubleSolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();
		
		String varFile = "nsgaii-double-problem-variable.tsv";
		String objFile = "nsgaii-double-problem-objective.tsv";

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext(varFile))
				.setFunFileOutputContext(new DefaultFileOutputContext(objFile)).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file " + varFile);
		JMetalLogger.logger.info("Variables values have been written to file " + objFile);

	}

}
