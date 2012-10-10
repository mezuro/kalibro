package org.kalibro;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of processing a {@link Metric} for a {@link Module}. Contains the associated {@link MetricConfiguration}
 * snapshot.
 * 
 * @author Carlos Morais
 */
public class MetricResult extends AbstractMetricResult {

	private MetricConfiguration configuration;
	private Throwable error;
	private List<Double> descendentResults;

	public MetricResult(MetricConfiguration configuration, Throwable error) {
		this(configuration, Double.NaN);
		this.error = error;
	}

	public MetricResult(MetricConfiguration configuration, Double value) {
		super(configuration.getMetric(), value);
		this.configuration = configuration;
		setDescendentResults(new ArrayList<Double>());
	}

	public boolean hasError() {
		return error != null;
	}

	public Throwable getError() {
		return error;
	}

	public MetricConfiguration getConfiguration() {
		return configuration;
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

	public boolean hasRange() {
		return configuration.getRangeFor(getAggregatedValue()) != null;
	}

	public Range getRange() {
		return configuration.getRangeFor(getAggregatedValue());
	}

	public boolean hasGrade() {
		return hasRange() && getRange().hasReading();
	}

	public Double getGrade() {
		return getRange().getReading().getGrade();
	}

	public Double getWeight() {
		return configuration.getWeight();
	}
}