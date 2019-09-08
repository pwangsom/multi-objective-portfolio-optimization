package com.amontep.portfolio;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import com.amontep.portfolio.problem.PortfolioIntegerConstrainedProblem;

public class NsgaiiiIntegerProblemRunner extends AbstractAlgorithmRunner {

	public static void main(String[] args) throws JMetalException {
		Problem<IntegerSolution> problem;
		Algorithm<List<IntegerSolution>> algorithm;
		CrossoverOperator<IntegerSolution> crossover;
		MutationOperator<IntegerSolution> mutation;
		SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;

		problem = new PortfolioIntegerConstrainedProblem();

		double crossoverProbability = 0.9;
		double crossoverDistributionIndex = 30.0;
		crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<IntegerSolution>();

		algorithm = new NSGAIIIBuilder<>(problem).setCrossoverOperator(crossover).setMutationOperator(mutation)
				.setSelectionOperator(selection).setMaxIterations(100000).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<IntegerSolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();

		String varFile = "nsgaiii-integer-problem-variable.tsv";
		String objFile = "nsgaiii-integer-problem-objective.tsv";

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext(varFile))
				.setFunFileOutputContext(new DefaultFileOutputContext(objFile)).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file " + varFile);
		JMetalLogger.logger.info("Variables values have been written to file " + objFile);
	}

}
