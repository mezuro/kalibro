package org.kalibro.dto;

import java.util.SortedSet;

import org.kalibro.*;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.RangeDao;
import org.kalibro.dao.ReadingGroupDao;

/**
 * Data transfer object for {@link MetricConfiguration}.
 * 
 * @author Carlos Morais
 */
public abstract class MetricConfigurationDto extends DataTransferObject<MetricConfiguration> {

	@Override
	public MetricConfiguration convert() {
		Metric metric = metric();
		MetricConfiguration metricConfiguration = metric.isCompound() ? convertCompound(metric) : convertNative(metric);
		setId(metricConfiguration, id());
		metricConfiguration.setCode(code());
		metricConfiguration.setWeight(weight());
		metricConfiguration.setAggregationForm(aggregationForm());
		metricConfiguration.setReadingGroup(readingGroupId() == null ? null : readingGroup());
		metricConfiguration.setRanges(ranges());
		return metricConfiguration;
	}

	public MetricConfiguration convertCompound(Metric metric) {
		return new MetricConfiguration((CompoundMetric) metric);
	}

	public MetricConfiguration convertNative(Metric metric) {
		return new MetricConfiguration(baseTool(), (NativeMetric) metric);
	}

	public abstract Metric metric();

	protected BaseTool baseTool() {
		return DaoLazyLoader.createProxy(BaseToolDao.class, "get", baseToolName());
	}

	public abstract String baseToolName();

	public abstract Long id();

	public abstract String code();

	public abstract Double weight();

	public abstract Statistic aggregationForm();

	public ReadingGroup readingGroup() {
		return DaoLazyLoader.createProxy(ReadingGroupDao.class, "get", readingGroupId());
	}

	public abstract Long readingGroupId();

	public SortedSet<Range> ranges() {
		return DaoLazyLoader.createProxy(RangeDao.class, "rangesOf", id());
	}
}