package com.amontep.portfolio.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import com.amontep.portfolio.stock.Stock;

@SuppressWarnings("serial")
public class PortfolioIntegerConstrainedProblem extends AbstractIntegerProblem implements ConstrainedProblem<IntegerSolution> {
	
	private final int NO_OBJECTIVES = 3;
	private final int NO_CONSTRAINTS = 1;
	private final int LOWER_BOUND = 0;
	private final int UPPER_BOUND = 7000;

	private Double totalWeight = 0.0;
	
	public OverallConstraintViolation<IntegerSolution> overallConstraintViolationDegree;
	public NumberOfViolatedConstraints<IntegerSolution> numberOfViolatedConstraints;
	
	public PortfolioIntegerConstrainedProblem() throws JMetalException {

		setNumberOfVariables(Stock.getExpectedReturns().length);
		setNumberOfObjectives(NO_OBJECTIVES);
		setNumberOfConstraints(NO_CONSTRAINTS);
		setName("PortfolioIntegerProblem");

		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(LOWER_BOUND);
			upperLimit.add(UPPER_BOUND);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
		
	    overallConstraintViolationDegree = new OverallConstraintViolation<IntegerSolution>();
	    numberOfViolatedConstraints = new NumberOfViolatedConstraints<IntegerSolution>();
	}

	@Override
	public void evaluate(IntegerSolution solution) {
		// TODO Auto-generated method stub
		
		Double expectedReturn = 0.0;
		Double risk = 0.0;

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
        	
        	Double weigth = 0.0;        	
        	if(solution.getVariableValue(i) > 500 && solution.getVariableValue(i) < 2001) weigth = solution.getVariableValue(i) / 10000.0;
        	
            expectedReturn += weigth * Stock.getExpectedReturns()[i];
            totalWeight += weigth;

        }

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
        	Double weigth = 0.0;        	
        	if(solution.getVariableValue(i) > 500 && solution.getVariableValue(i) < 2001) weigth = solution.getVariableValue(i) / 10000.0;
        	
            for (int j = 0; j < solution.getNumberOfVariables(); j++) {           	
            	
                risk += weigth * weigth * Stock.getCovarianceMatrix()[i][j];
            }
        }

		solution.setObjective(0, expectedReturn * -1);
		solution.setObjective(1, risk);
		solution.setObjective(2, 0.0);		
	}

	@Override
	public void evaluateConstraints(IntegerSolution solution) {
		// TODO Auto-generated method stub
	    Double constraintWeight = 0.0;
	    
	    if(totalWeight > 1.0) {
	    	constraintWeight = 1.0 - totalWeight;
	    } else if (totalWeight < 1.0) {
	    	constraintWeight = totalWeight - 1.0;	    	
	    }

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
