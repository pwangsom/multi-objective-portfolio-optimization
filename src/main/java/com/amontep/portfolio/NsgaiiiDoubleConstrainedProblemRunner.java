package com.amontep.portfolio;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
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
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import com.amontep.portfolio.problem.PorfolioDoubleConstrainedProblem;

/**
 * Class to configure and run the NSGA-III algorithm
 */
public class NsgaiiiDoubleConstrainedProblemRunner extends AbstractAlgorithmRunner {
	/**
	 * @param args Command line arguments.
	 * @throws java.io.IOException
	 * @throws SecurityException
	 * @throws ClassNotFoundException Usage: three options -
	 *                                org.uma.jmetal.runner.multiobjective.NSGAIIIRunner
	 *                                -
	 *                                org.uma.jmetal.runner.multiobjective.NSGAIIIRunner
	 *                                problemName -
	 *                                org.uma.jmetal.runner.multiobjective.NSGAIIIRunner
	 *                                problemName paretoFrontFile
	 */
	public static void main(String[] args) throws JMetalException {
		
		Problem<DoubleSolution> problem;
		Algorithm<List<DoubleSolution>> algorithm;
		CrossoverOperator<DoubleSolution> crossover;
		MutationOperator<DoubleSolution> mutation;
		SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

		problem = new PorfolioDoubleConstrainedProblem();

		double crossoverProbability = 0.9;
		double crossoverDistributionIndex = 30.0;
		crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<DoubleSolution>();

		algorithm = new NSGAIIIBuilder<>(problem).setCrossoverOperator(crossover).setMutationOperator(mutation)
				.setSelectionOperator(selection).setMaxIterations(20000).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<DoubleSolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();

		String varFile = "nsgaiii-constrained-double-problem-variable.tsv";
		String objFile = "nsgaiii-constrained-double-problem-objective.tsv";

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext(varFile))
				.setFunFileOutputContext(new DefaultFileOutputContext(objFile)).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file " + varFile);
		JMetalLogger.logger.info("Variables values have been written to file " + objFile);
	}
}
