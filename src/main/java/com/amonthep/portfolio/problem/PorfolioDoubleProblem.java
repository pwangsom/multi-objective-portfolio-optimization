package com.amonthep.portfolio.problem;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import com.amonthep.portfolio.stock.Stock;

@SuppressWarnings({"serial"})
public class PorfolioDoubleProblem extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
	
	private final int NO_OBJECTIVES = 3;
	private final int NO_CONSTRAINTS = 1;
	private final double LOWER_BOUND = 0.0;
	private final double UPPER_BOUND = 0.25;
	
	private List<Stock> stockList;
	private Double totalWeight = 0.0;
	
	public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
	public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

	public PorfolioDoubleProblem(List<Stock> stockList) {
		this.stockList = stockList;
		
		setNumberOfVariables(this.stockList.size());
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
		
		Double totalReturn = 0.0;
		Double totalRisk = 0.0;
		
		for(int i = 0; i < getNumberOfVariables(); i++) {
			
			totalReturn += solution.getVariableValue(i) * stockList.get(i).getReturnProfit();
			totalRisk += solution.getVariableValue(i) * stockList.get(i).getRisk();
			
			totalWeight += solution.getVariableValue(i);
			
		}
		
		solution.setObjective(0, totalReturn);
		solution.setObjective(1, totalRisk);
		solution.setObjective(2, 0.0);
	}

	@Override
	public void evaluateConstraints(DoubleSolution solution) {
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
