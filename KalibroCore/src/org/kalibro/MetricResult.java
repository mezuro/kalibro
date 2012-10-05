package org.kalibro;

import java.util.ArrayList;
import java.util.List;

import org.kalibro.core.abstractentity.Ignore;

/**
 * Result of processing a {@link Metric} for a {@link Module}. Contains the associated {@link MetricConfiguration}
 * snapshot.
 * 
 * @author Carlos Morais
 */
public class MetricResult extends AbstractMetricResult {

	@Ignore
	private MetricConfiguration configuration;

	private Throwable error;
	private List<Double> descendentResults;

	public MetricResult(MetricConfiguration configuration, Double value) {
		super(configuration.getMetric(), value);
		this.configuration = configuration;
		setDescendentResults(new ArrayList<Double>());
	}

	public MetricResult(CompoundMetric metric, Throwable error) {
		super(metric, Double.NaN);
		this.error = error;
	}

	public boolean hasError() {
		return error != null;
	}

	public Throwable getError() {
		return error;
	}

	public List<Double> getDescendentResults() {
		return descendentResults;
	}

	public void setDescendentResults(List<Double> descendentResults) {
		this.descendentResults = descendentResults;
	}

	public void addDescendentResult(Double descendentResult) {
		descendentResults.add(descendentResult);
	}

	public Double getAggregatedValue() {
		if (getValue().isNaN() && !descendentResults.isEmpty())
			return configuration.getAggregationForm().calculate(descendentResults);
		return getValue();
	}

	public boolean hasGrade() {
		Range range = configuration.getRangeFor(getValue());
		return range != null && range.getReading() != null;
	}

	public Double getGrade() {
		return configuration.getRangeFor(getValue()).getReading().getGrade();
	}

	public Double getWeight() {
		return configuration.getWeight();
	}
}