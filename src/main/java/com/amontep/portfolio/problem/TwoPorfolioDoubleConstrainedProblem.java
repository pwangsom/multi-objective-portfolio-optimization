package com.amontep.portfolio.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import com.amontep.portfolio.stock.Stock;

@SuppressWarnings("serial")
public class TwoPorfolioDoubleConstrainedProblem extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
	
	private final int NO_OBJECTIVES = 2;
	private final int NO_CONSTRAINTS = 1;
	private final double LOWER_BOUND = -0.25;
	private final double UPPER_BOUND = 0.25;
	
	private Double totalWeight = 0.0;
	
	public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
	public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

	public TwoPorfolioDoubleConstrainedProblem() throws JMetalException {
		
		setNumberOfVariables(Stock.getExpectedReturns().length);
		setNumberOfObjectives(NO_OBJECTIVES);
		setNumberOfConstraints(NO_CONSTRAINTS);
		setName("PorfolioDoubleProblem");

		List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(LOWER_BOUND);
			upperLimit.add(UPPER_BOUND);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
		
	    overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
	    numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
	}

	@Override
	public void evaluate(DoubleSolution solution) {
		// TODO Auto-generated method stub
		
		Double expectedReturn = 0.0;
		Double risk = 0.0;

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
        	
        	Double weigth = 0.0;        	
        	if(solution.getVariableValue(i).compareTo(Double.valueOf(0.0)) > 0) weigth = solution.getVariableValue(i);
        	
            expectedReturn += weigth * Stock.getExpectedReturns()[i];
            totalWeight += weigth;

        }

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
        	Double weigth = 0.0;        	
        	if(solution.getVariableValue(i).compareTo(Double.valueOf(0.0)) > 0) weigth = solution.getVariableValue(i);
        	
            for (int j = 0; j < solution.getNumberOfVariables(); j++) {           	
            	
                risk += weigth * weigth * Stock.getCovarianceMatrix()[i][j];
            }
        }
		
		solution.setObjective(0, expectedReturn * -1);
		solution.setObjective(1, risk);
	}

	@Override
	public void evaluateConstraints(DoubleSolution solution) {
		// TODO Auto-generated method stub
		
	    Double constraintWeight = 0.0;
	    
	    if(totalWeight > 1.0) {
	    	constraintWeight = 1.0 - totalWeight;
	    } else if (totalWeight < 1.0) { constraintWeight = totalWeight - 1.0; }

	    double overallConstraintViolation = 0.0;	    
	    int violatedConstraints = 0;
	    
		if (constraintWeight < 0.0) {
			overallConstraintViolation += constraintWeight;
			violatedConstraints++;
		}

	    overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
	    numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
	    
	}	
	
	
}
