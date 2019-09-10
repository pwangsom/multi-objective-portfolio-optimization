package com.amontep.portfolio;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.GeneralizedSpread;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import com.amontep.portfolio.front.ModifiedFrontNormalizer;
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
				.setSelectionOperator(selection).setMaxIterations(3000).build();

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
		
		evaluateHypervolume(getReferencePareto(problem), population);
	}
	
	private static void evaluateHypervolume(List<DoubleSolution> referencePareto, List<DoubleSolution> population) {		
		Front referenceFront = new ArrayFront(referencePareto);
		Front front = new ArrayFront(population);
		
		System.out.println("printQualityIndicator");
		printQualityIndicator(referenceFront, front, false);
		System.out.println("");
		System.out.println("printNormalizedQualityIndicator");
		printQualityIndicator(referenceFront, front, true);
		
	}
	
	private static void printQualityIndicator(Front referenceFront, Front front, boolean normalize) {		
		if(normalize) {			
			ModifiedFrontNormalizer frontNormalizer = new ModifiedFrontNormalizer(referenceFront);
			referenceFront = frontNormalizer.normalize(referenceFront);
			front = frontNormalizer.normalize(front);			
		}
		
		List<QualityIndicator<List<PointSolution>, Double>> indicatorList = getAvailableIndicators(referenceFront);

		for (QualityIndicator<List<PointSolution>, Double> indicator : indicatorList) {
			System.out.println(
					indicator.getName() + ": " + indicator.evaluate(FrontUtils.convertFrontToSolutionList(front)));
		}	
		
	}
	
	private static List<QualityIndicator<List<PointSolution>, Double>> getAvailableIndicators(Front referenceFront) {

		List<QualityIndicator<List<PointSolution>, Double>> list = new ArrayList<>();
		list.add(new Epsilon<PointSolution>(referenceFront));
		list.add(new PISAHypervolume<PointSolution>(referenceFront));
		list.add(new GenerationalDistance<PointSolution>(referenceFront));
		list.add(new InvertedGenerationalDistance<PointSolution>(referenceFront));
		list.add(new InvertedGenerationalDistancePlus<PointSolution>(referenceFront));
		list.add(new Spread<PointSolution>(referenceFront));
		list.add(new GeneralizedSpread<PointSolution>(referenceFront));
		list.add(new ErrorRatio<List<PointSolution>>(referenceFront));

		return list;
	}
	
	private static List<DoubleSolution> getReferencePareto(Problem<DoubleSolution> problem) {
		
		DoubleSolution refSolution = problem.createSolution();
		
		for(int i = 0; i < refSolution.getNumberOfVariables(); i++) {
			refSolution.setVariableValue(i, 0.0);
		}
		
		refSolution.setObjective(0, 0.0);
		refSolution.setObjective(1, 0.0);
		refSolution.setObjective(2, 0.0);
		
		return Arrays.asList(refSolution);
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
