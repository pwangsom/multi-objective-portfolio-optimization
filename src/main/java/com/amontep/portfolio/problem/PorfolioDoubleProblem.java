package com.amontep.portfolio.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import com.amontep.portfolio.problem.evaluator.PorfolioDoubleProblemEvaluator;
import com.amontep.portfolio.stock.Stock;

@SuppressWarnings("serial")
public class PorfolioDoubleProblem extends AbstractDoubleProblem {
	
	private final int NO_OBJECTIVES = 3;
	private final double LOWER_BOUND = 0.05;
	private final double UPPER_BOUND = 0.20;

	public PorfolioDoubleProblem() throws JMetalException {

		setNumberOfVariables(Stock.getExpectedReturns().length);
		setNumberOfObjectives(NO_OBJECTIVES);
		setName("PorfolioDoubleProblem");

		List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(LOWER_BOUND);
			upperLimit.add(UPPER_BOUND);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	@Override
	public void evaluate(DoubleSolution solution) {
		// TODO Auto-generated method stub
		
		PorfolioDoubleProblemEvaluator evaluator = new PorfolioDoubleProblemEvaluator(solution);
		evaluator.createAdjustedSolution();
		evaluator.evaluate();

		solution.setObjective(0, evaluator.getExpectedReturn() * -1);
		solution.setObjective(1, evaluator.getRisk());
		solution.setObjective(2, 0.0);
	}

}
