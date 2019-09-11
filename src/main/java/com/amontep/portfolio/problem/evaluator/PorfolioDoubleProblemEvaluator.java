package com.amontep.portfolio.problem.evaluator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.solution.DoubleSolution;

import com.amontep.portfolio.stock.Stock;

import lombok.Getter;

@Getter
public class PorfolioDoubleProblemEvaluator {
	
	private Double expectedReturn = 0.0;
	private Double risk = 0.0;
	private DoubleSolution solution;
	private DoubleSolution adjustedsolution;
	
	private final Double MAX_WEIGHT = 1.0;
	private Integer maxStock = 10;
	
	private List<DoubleGene> originGenes;
	private List<DoubleGene> newGenes;
	
	public PorfolioDoubleProblemEvaluator(DoubleSolution solution) {
		this.solution = solution;
		this.adjustedsolution = (DoubleSolution) solution.copy();

		originGenes = new ArrayList<DoubleGene>();
		newGenes = new ArrayList<DoubleGene>();
	}

	public PorfolioDoubleProblemEvaluator(DoubleSolution solution, Integer noOfStock) {
		this(solution);		
		this.maxStock = noOfStock;
	}
	
	public void createAdjustedSolution() {
		convertSolutionToGene();
		
		// Sort fullGens by solutionValue: Largest -> smallest		
		originGenes = originGenes.stream().sorted(Comparator.comparingDouble(DoubleGene::getSolutionValue).reversed()).collect(Collectors.toList());
		
		//findTopGenesWithinMaxWeight();
		
		findTopStock();
		
		updateSolutionNew();		
	}
	
	public void evaluate() {	
        for (int i = 0; i < adjustedsolution.getNumberOfVariables(); i++) {        	
            expectedReturn += adjustedsolution.getVariableValue(i) * Stock.getExpectedReturns()[i];
        }

        for (int i = 0; i < adjustedsolution.getNumberOfVariables(); i++) {
            for (int j = 0; j < adjustedsolution.getNumberOfVariables(); j++) {           	            	
                risk += adjustedsolution.getVariableValue(i) * adjustedsolution.getVariableValue(i) * Stock.getCovarianceMatrix()[i][j];
            }
        }	
	}
	
	private void updateSolutionNew() {		
		newGenes.stream().forEach(gene -> {			
			adjustedsolution.setVariableValue(gene.getSolutionIndex(), gene.getSolutionValue());			
		});		
	}
	
	private void findTopStock() {
		
		int count = 0;
		Double topStockWeight = 0.0;
		
		for(DoubleGene gene : originGenes) {	
        	DoubleGene clone = new DoubleGene();
        	clone.setSolutionIndex(gene.getSolutionIndex());
			
			if(count < maxStock) {
				topStockWeight += gene.getSolutionValue();
				clone.setSolutionValue(gene.getSolutionValue());
			} else {	
	        	clone.setSolutionValue(0.0);	
			}		
			
			newGenes.add(clone);
			count++;		
		}
		
		
		if(!(topStockWeight.compareTo(MAX_WEIGHT) == 0)) {
			Double avgAdjusted = Math.abs(topStockWeight - MAX_WEIGHT) / maxStock;
			
			if(topStockWeight.compareTo(MAX_WEIGHT) < 0) {
				for(int i = 0; i < maxStock; i++) {
					newGenes.get(i).setSolutionValue(newGenes.get(i).getSolutionValue() + avgAdjusted);
				}
			} else {
				for(int i = 0; i < maxStock; i++) {
					newGenes.get(i).setSolutionValue(newGenes.get(i).getSolutionValue() - avgAdjusted);
				}
			}
		}
		
	}
	
	private void findTopGenesWithinMaxWeight() {		
		Double currentWeight = 0.0;

		for(DoubleGene gene : originGenes) {	
        	DoubleGene clone = new DoubleGene();
        	clone.setSolutionIndex(gene.getSolutionIndex());
			
			if(currentWeight <= MAX_WEIGHT) {
				if((currentWeight + gene.getSolutionValue()) <= MAX_WEIGHT) {
					clone.setSolutionValue(gene.getSolutionValue());
				} else {
					clone.setSolutionValue(MAX_WEIGHT - currentWeight);
				}								
			} else {	
	        	clone.setSolutionValue(0.0);	
			}		
			
			newGenes.add(clone);
			currentWeight += gene.getSolutionValue();		
		}
		
	}
	
	
	private void convertSolutionToGene() {		
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {        	
        	DoubleGene gene = new DoubleGene();
        	gene.setSolutionIndex(i);
        	gene.setSolutionValue(solution.getVariableValue(i));
        	
        	originGenes.add(gene);
        }
	}
	
}
