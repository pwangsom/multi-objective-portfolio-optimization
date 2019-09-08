package com.amontep.portfolio;

import java.util.ArrayList;
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
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import com.amontep.portfolio.problem.PorfolioDoubleProblem;
import com.amontep.portfolio.problem.evaluator.PorfolioDoubleProblemEvaluator;

public class NsgaiiiDoubleProblemRunner {

	public static void main(String[] args) throws JMetalException {
		
		Problem<DoubleSolution> problem;
		Algorithm<List<DoubleSolution>> algorithm;
		CrossoverOperator<DoubleSolution> crossover;
		MutationOperator<DoubleSolution> mutation;
		SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

		problem = new PorfolioDoubleProblem();

		double crossoverProbability = 0.9;
		double crossoverDistributionIndex = 30.0;
		crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		double mutationDistributionIndex = 20.0;
		mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<DoubleSolution>();

		algorithm = new NSGAIIIBuilder<>(problem).setCrossoverOperator(crossover).setMutationOperator(mutation)
				.setSelectionOperator(selection).setMaxIterations(300).build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List<DoubleSolution> population = algorithm.getResult();
		long computingTime = algorithmRunner.getComputingTime();

		String varFile = "nsgaiii-double-problem-variable.tsv";
		String objFile = "nsgaiii-double-problem-objective.tsv";

		new SolutionListOutput(population).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext(varFile))
				.setFunFileOutputContext(new DefaultFileOutputContext(objFile)).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Variables values have been written to file " + varFile);
		JMetalLogger.logger.info("Objectives values have been written to file " + objFile);
		
		printAdjustedWeightSolution(population);
	}
	
	private static void printAdjustedWeightSolution(List<DoubleSolution> population) {
		
		List<DoubleSolution> adjustedPopulation = new ArrayList<DoubleSolution>();
		
		population.stream().forEach(solution -> {			
			PorfolioDoubleProblemEvaluator evaluator = new PorfolioDoubleProblemEvaluator(solution);
			evaluator.createAdjustedSolution();
			
			adjustedPopulation.add(evaluator.getAdjustedsolution());
			
		});		

		String varFile = "nsgaiii-double-problem-adjusted-variable.tsv";

		new SolutionListOutput(adjustedPopulation).setSeparator("\t")
				.setVarFileOutputContext(new DefaultFileOutputContext(varFile)).print();		

		JMetalLogger.logger.info("Adjusted Variables values have been written to file " + varFile);
		
	}
	
}
