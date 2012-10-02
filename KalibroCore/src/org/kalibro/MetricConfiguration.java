package org.kalibro;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.Identifier;
import org.kalibro.core.abstractentity.*;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;

/**
 * Configuration for a metric. Contains weight, aggregation form and a set of evalutation ranges.
 * 
 * @author Carlos Morais
 */
@SortingFields("code")
public class MetricConfiguration extends AbstractEntity<MetricConfiguration> {

	@Print(skip = true)
	private Long id;

	@IdentityField
	private String code;

	private Metric metric;
	private BaseTool baseTool;

	private Double weight;
	private Statistic aggregationForm;
	private ReadingGroup readingGroup;
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
		setReadingGroup(null);
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
	}

	private void assertNoCodeConflict(MetricConfiguration other, String theCode) {
		if (other.code.equals(theCode))
			throw new KalibroException("Metric with code '" + theCode + "' already exists in the configuration.");
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

	public ReadingGroup getReadingGroup() {
		return readingGroup;
	}

	public void setReadingGroup(ReadingGroup readingGroup) {
		this.readingGroup = readingGroup;
	}

	public SortedSet<Range> getRanges() {
		for (Range range : ranges)
			range.setConfiguration(this);
		return new TreeSet<Range>(ranges);
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

	public void addRange(Range newRange) {
		for (Range range : ranges)
			if (range.intersectsWith(newRange))
				throw new KalibroException("New range " + newRange + " would conflict with " + range);
		newRange.setConfiguration(this);
		ranges.add(newRange);
	}

	public void removeRange(Range range) {
		ranges.remove(range);
		range.setConfiguration(null);
	}

	public Long getConfigurationId() {
		return configuration.getId();
	}

	void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void save() {
		if (code.trim().isEmpty())
			throw new KalibroException("Metric configuration requires code.");
		if (configuration == null)
			throw new KalibroException("Metric is not in any configuration.");
		if (!configuration.hasId())
			throw new KalibroException("Configuration is not saved. Save configuration instead");
		id = dao().save(this);
		readingGroup = DaoFactory.getReadingGroupDao().readingGroupOf(id);
		ranges = DaoFactory.getRangeDao().rangesOf(id);
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		if (configuration != null)
			configuration.removeMetricConfiguration(this);
		deleted();
	}

	void deleted() {
		for (Range range : ranges)
			range.deleted();
		id = null;
	}

	private MetricConfigurationDao dao() {
		return DaoFactory.getMetricConfigurationDao();
	}
}