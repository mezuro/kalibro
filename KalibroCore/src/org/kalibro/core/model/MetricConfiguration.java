package org.kalibro.core.model;

import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.core.util.Identifier;

@SortingMethods("getCode")
public class MetricConfiguration extends AbstractEntity<MetricConfiguration> {

	@IdentityField
	private String code;

	private Metric metric;
	private Double weight;
	private Statistic aggregationForm;
	private SortedSet<Range> ranges;

	public MetricConfiguration(Metric metric) {
		setCode(Identifier.fromText(metric.getName()).asVariable());
		setMetric(metric);
		setWeight(1.0);
		setAggregationForm(Statistic.AVERAGE);
		ranges = new TreeSet<Range>();
	}

	public void assertNoConflictWith(MetricConfiguration other) {
		if (other.code.equals(this.code))
			throw new IllegalArgumentException("A metric configuration with the same code already exists");
		else if (other.metric.equals(this.metric))
			throw new IllegalArgumentException("There is already a configuration for this metric");
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Statistic getAggregationForm() {
		return aggregationForm;
	}

	public void setAggregationForm(Statistic aggregationForm) {
		this.aggregationForm = aggregationForm;
	}

	public SortedSet<Range> getRanges() {
		return ranges;
	}

	public boolean hasRangeFor(Double value) {
		try {
			getRangeFor(value);
			return true;
		} catch (IllegalArgumentException exception) {
			return false;
		}
	}

	public Range getRangeFor(Double value) {
		for (Range range : ranges)
			if (range.contains(value))
				return range;
		throw new IllegalArgumentException("No range found for value " + value);
	}

	public void addRange(Range newRange) {
		for (Range range : ranges)
			if (range.intersectsWith(newRange))
				throw new IllegalArgumentException("New range " + newRange + " would conflict with " + range);
		ranges.add(newRange);
	}

	public boolean removeRange(Range range) {
		return ranges.remove(range);
	}
}