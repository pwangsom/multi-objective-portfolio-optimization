package com.amontep.portfolio.front;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;

public class ModifiedFrontNormalizer extends FrontNormalizer {

	private double[] maximumValues;
	private double[] minimumValues;

	public ModifiedFrontNormalizer(List<? extends Solution<?>> referenceFront) {
		super(referenceFront);				

	    maximumValues = FrontUtils.getMaximumValues(new ArrayFront(referenceFront));
	    minimumValues = FrontUtils.getMinimumValues(new ArrayFront(referenceFront));
	}
	
	public ModifiedFrontNormalizer(Front referenceFront) {
		super(referenceFront);
		
	    maximumValues = FrontUtils.getMaximumValues(referenceFront);
	    minimumValues = FrontUtils.getMinimumValues(referenceFront);
	}
	
	public ModifiedFrontNormalizer(double[] minimumValues, double[] maximumValues) {
		super(minimumValues, maximumValues);
		// TODO Auto-generated constructor stub
		
	    this.maximumValues = maximumValues ;
	    this.minimumValues = minimumValues ;
	}
	
	@Override
	public List<? extends Solution<?>> normalize(List<? extends Solution<?>> solutionList) {
		Front normalizedFront;
		if (solutionList == null) {
			throw new JMetalException("The front is null");
		}

		normalizedFront = getNormalizedFront(new ArrayFront(solutionList), maximumValues, minimumValues);

		return FrontUtils.convertFrontToSolutionList(normalizedFront);
	}
	
	@Override
	public Front normalize(Front front) {
		if (front == null) {
			throw new JMetalException("The front is null");
		}

		return getNormalizedFront(front, maximumValues, minimumValues);
	}
	
	private Front getNormalizedFront(Front front, double[] maximumValues, double[] minimumValues) {
		if (front.getNumberOfPoints() == 0) {
			throw new JMetalException("The front is empty");
		} else if (front.getPoint(0).getDimension() != maximumValues.length) {
			throw new JMetalException("The length of the point dimensions (" + front.getPoint(0).getDimension()
					+ ") " + "is different from the length of the maximum array (" + maximumValues.length + ")");
		}

		Front normalizedFront = new ArrayFront(front);
		int numberOfPointDimensions = front.getPoint(0).getDimension();

		for (int i = 0; i < front.getNumberOfPoints(); i++) {
			for (int j = 0; j < numberOfPointDimensions; j++) {
				
				if ((maximumValues[j] - minimumValues[j]) == 0) {
					normalizedFront.getPoint(i).setValue(j, 0.0);
				} else {
					normalizedFront.getPoint(i).setValue(j,
							(front.getPoint(i).getValue(j) - minimumValues[j])
									/ (maximumValues[j] - minimumValues[j]));
				}
			}
		}
		return normalizedFront;
	}
}
