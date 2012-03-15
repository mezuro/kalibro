package org.kalibro.service.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.core.util.DataTransferObject;

@XmlRootElement(name = "MetricConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricConfigurationXml implements DataTransferObject<MetricConfiguration> {

	@XmlElement(required = true)
	private MetricXml<?> metric;

	@XmlElement(required = true)
	private String code;

	private Double weight;

	private Statistic aggregationForm;

	@XmlElement(name = "range")
	private Collection<RangeXml> ranges;

	public MetricConfigurationXml() {
		super();
	}

	public MetricConfigurationXml(MetricConfiguration metricConfiguration) {
		initializeMetric(metricConfiguration);
		code = metricConfiguration.getCode();
		weight = metricConfiguration.getWeight();
		aggregationForm = metricConfiguration.getAggregationForm();
		initializeRanges(metricConfiguration);
	}

	private void initializeMetric(MetricConfiguration metricConfiguration) {
		Metric theMetric = metricConfiguration.getMetric();
		if (theMetric.isCompound())
			metric = new CompoundMetricXml((CompoundMetric) theMetric);
		else
			metric = new NativeMetricXml((NativeMetric) theMetric);
	}

	private void initializeRanges(MetricConfiguration metricConfiguration) {
		ranges = new ArrayList<RangeXml>();
		for (Range range : metricConfiguration.getRanges())
			ranges.add(new RangeXml(range));
	}

	@Override
	public MetricConfiguration convert() {
		MetricConfiguration metricConfiguration = new MetricConfiguration(metric.convert());
		metricConfiguration.setCode(code);
		metricConfiguration.setWeight(weight);
		metricConfiguration.setAggregationForm(aggregationForm);
		convertRanges(metricConfiguration);
		return metricConfiguration;
	}

	private void convertRanges(MetricConfiguration metricConfiguration) {
		for (RangeXml range : ranges)
			metricConfiguration.addRange(range.convert());
	}
}