package org.kalibro.core.model;

import java.util.ArrayList;
import java.util.Collection;

import org.kalibro.KalibroException;
import org.kalibro.core.model.enums.Statistic;

public class MetricResult extends AbstractMetricResult {

	private Range range;
	private Double weight;
	private Collection<Double> descendentResults = new ArrayList<Double>();

	public MetricResult(NativeMetricResult nativeResult) {
		this(nativeResult.getMetric(), nativeResult.getValue());
	}

	public MetricResult(Metric metric, Double value) {
		super(metric, value);
	}

	public void setConfiguration(MetricConfiguration configuration) {
		if (value.isNaN() && hasStatistics())
			value = getStatistic(configuration.getAggregationForm());
		if (configuration.hasRangeFor(value))
			setRange(configuration.getRangeFor(value));
		setWeight(configuration.getWeight());
	}

	public boolean hasRange() {
		return range != null;
	}

	public Double getGrade() {
		return getRange().getGrade();
	}

	public Range getRange() {
		if (range == null)
			throw new KalibroException("No range found for metric '" + metric + "' and value " + value);
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public boolean hasStatistics() {
		return !descendentResults.isEmpty();
	}

	public Double getStatistic(Statistic statistic) {
		return statistic.calculate(descendentResults);
	}

	public Collection<Double> getDescendentResults() {
		return descendentResults;
	}

	public void addDescendentResult(Double descendentResult) {
		descendentResults.add(descendentResult);
	}

	public void addDescendentResults(Collection<Double> results) {
		descendentResults.addAll(results);
	}
}