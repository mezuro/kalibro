package org.kalibro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kalibro.core.Identifier;

/**
 * Rules to extract a result from a collection of values.
 * 
 * @author Carlos Morais
 */
public enum Statistic {

	AVERAGE {

		@Override
		public Double calculate(Collection<Double> values) {
			return SUM.calculate(values) / values.size();
		}
	},
	COUNT {

		@Override
		public Double calculate(Collection<Double> values) {
			return new Double(values.size());
		}
	},
	MAXIMUM {

		@Override
		public Double calculate(Collection<Double> values) {
			Double maximum = Double.NEGATIVE_INFINITY;
			for (Double value : values)
				maximum = Math.max(maximum, value);
			return maximum;
		}
	},
	MEDIAN {

		@Override
		public Double calculate(Collection<Double> values) {
			if (values.isEmpty())
				return Double.NaN;
			List<Double> sortedNumbers = new ArrayList<Double>(values);
			Collections.sort(sortedNumbers);
			return median(sortedNumbers);
		}

		private Double median(List<Double> orderedList) {
			double middle = (orderedList.size() - 1) / 2.0;
			int left = (int) Math.floor(middle);
			int right = (int) Math.ceil(middle);
			return (orderedList.get(left) + orderedList.get(right)) / 2.0;
		}
	},
	MINIMUM {

		@Override
		public Double calculate(Collection<Double> values) {
			Double minimum = Double.POSITIVE_INFINITY;
			for (Double value : values)
				minimum = Math.min(minimum, value);
			return minimum;
		}
	},
	STANDARD_DEVIATION {

		@Override
		public Double calculate(Collection<Double> values) {
			Double average = AVERAGE.calculate(values);
			Double variance = 0.0;
			for (Double value : values)
				variance += Math.pow(value - average, 2);
			variance /= values.size();
			return Math.sqrt(variance);
		}
	},
	SUM {

		@Override
		public Double calculate(Collection<Double> values) {
			Double sum = 0.0;
			for (Double value : values)
				sum += value;
			return sum;
		}
	};

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public abstract Double calculate(Collection<Double> values);
}