package org.kalibro;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.Identifier;
import org.kalibro.core.abstractentity.*;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;

/**
 * Configuration for a metric. Contains weight, aggregation form and a set of evaluation ranges.
 * 
 * @author Carlos Morais
 */
@SortingFields("code")
public class MetricConfiguration extends AbstractEntity<MetricConfiguration> {

	@Print(skip = true)
	private Long id;

	@IdentityField
	@Print(order = 1)
	private String code;

	@Print(order = 2)
	private Metric metric;

	@Print(order = 3)
	private BaseTool baseTool;

	@Print(order = 4)
	private Double weight;

	@Print(order = 5)
	private Statistic aggregationForm;

	@Print(order = 6)
	private ReadingGroup readingGroup;

	@Print(order = 7)
	private Set<Range> ranges;

	@Ignore
	private Configuration configuration;

	public MetricConfiguration() {
		this(new CompoundMetric());
	}

	public MetricConfiguration(CompoundMetric metric) {
		initialize(metric);
	}

	public MetricConfiguration(BaseTool baseTool, NativeMetric metric) {
		this.baseTool = baseTool;
		initialize(metric);
	}

	private void initialize(Metric theMetric) {
		metric = theMetric;
		setCode(Identifier.fromText(metric.getName()).asVariable());
		setWeight(1.0);
		setAggregationForm(Statistic.AVERAGE);
		setRanges(new TreeSet<Range>());
	}

	public Long getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		if (configuration != null)
			for (MetricConfiguration other : configuration.getMetricConfigurations())
				if (other != this)
					assertNoCodeConflict(other, code);
		this.code = code;
	}

	void assertNoConflictWith(MetricConfiguration other) {
		assertNoCodeConflict(other, code);
		throwExceptionIf(other.metric.equals(metric), "Metric already exists in the configuration: " + metric);
	}

	private void assertNoCodeConflict(MetricConfiguration other, String theCode) {
		throwExceptionIf(other.code.equals(theCode),
			"Metric with code '" + theCode + "' already exists in the configuration.");
	}

	public Metric getMetric() {
		return metric;
	}

	public BaseTool getBaseTool() {
		return baseTool;
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

	public boolean hasReadingGroup() {
		return readingGroup != null;
	}

	public ReadingGroup getReadingGroup() {
		return readingGroup;
	}

	public void setReadingGroup(ReadingGroup readingGroup) {
		this.readingGroup = readingGroup;
	}

	public SortedSet<Range> getRanges() {
		TreeSet<Range> myRanges = new TreeSet<Range>();
		for (Range range : ranges) {
			range.setConfiguration(this);
			myRanges.add(range);
		}
		return myRanges;
	}

	public void setRanges(SortedSet<Range> ranges) {
		this.ranges = ranges;
	}

	public Range getRangeFor(Double value) {
		for (Range range : getRanges())
			if (range.contains(value))
				return range;
		return null;
	}

	public void addRange(Range range) {
		for (Range each : ranges)
			each.assertNoIntersectionWith(range);
		range.setConfiguration(this);
		ranges.add(range);
	}

	public void removeRange(Range range) {
		ranges = getRanges();
		ranges.remove(range);
		range.setConfiguration(null);
	}

	void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	void assertSaved() {
		if (!hasId())
			save();
	}

	public void save() {
		throwExceptionIf(code.trim().isEmpty(), "Metric configuration requires code.");
		throwExceptionIf(configuration == null, "Metric is not in any configuration.");
		configuration.assertSaved();
		if (hasReadingGroup())
			readingGroup.assertSaved();
		id = dao().save(this, configuration.getId());
		for (Range range : getRanges())
			range.save();
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		if (configuration != null)
			configuration.removeMetricConfiguration(this);
		deleted();
	}

	void deleted() {
		id = null;
		for (Range range : ranges)
			range.deleted();
	}

	private MetricConfigurationDao dao() {
		return DaoFactory.getMetricConfigurationDao();
	}
}