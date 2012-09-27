package org.kalibro;

import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.Identifier;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

@SortingFields("code")
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
			throw new KalibroException("A metric configuration with code '" + code + "' already exists");
		else if (other.metric.equals(this.metric))
			throw new KalibroException("There is already a configuration for this metric: " + metric);
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
		return findRangeFor(value) != null;
	}

	public Range getRangeFor(Double value) {
		Range range = findRangeFor(value);
		if (range == null)
			throw new KalibroException("No range found for value " + value + " and metric '" + metric + "'");
		return range;
	}

	private Range findRangeFor(Double value) {
		for (Range range : ranges)
			if (range.contains(value))
				return range;
		return null;
	}

	public void addRange(Range newRange) {
		for (Range range : ranges)
			if (range.intersectsWith(newRange))
				throw new KalibroException("New range " + newRange + " would conflict with " + range);
		ranges.add(newRange);
	}

	public void replaceRange(Double oldBeginning, Range newRange) {
		Range oldRange = getRangeFor(oldBeginning);
		removeRange(oldRange);
		try {
			addRange(newRange);
		} catch (KalibroException exception) {
			addRange(oldRange);
			throw exception;
		}
	}

	public boolean removeRange(Range range) {
		return ranges.remove(range);
	}

	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub

	}

	void deleted() {
		// TODO Auto-generated method stub

	}
}