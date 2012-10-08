package org.kalibro.service.xml;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.*;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "metricConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricConfigurationXmlResponse extends DataTransferObject<MetricConfiguration> {

	@XmlElement(required = true)
	private MetricXml<?> metric;

	@XmlElement(required = true)
	private String code;

	private Double weight;
	private Statistic aggregationForm;

	@XmlElement(name = "range")
	private Collection<RangeXmlRequest> ranges;

	public MetricConfigurationXmlResponse() {
		super();
	}

	public MetricConfigurationXmlResponse(MetricConfiguration metricConfiguration) {
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
		ranges = new ArrayList<RangeXmlRequest>();
		for (Range range : metricConfiguration.getRanges())
			ranges.add(new RangeXmlRequest(range));
	}

	@Override
	public MetricConfiguration convert() {
		MetricConfiguration metricConfiguration = new MetricConfiguration();
		metricConfiguration.setCode(code);
		metricConfiguration.setWeight(weight == null ? 1.0 : weight);
		metricConfiguration.setAggregationForm(aggregationForm == null ? Statistic.AVERAGE : aggregationForm);
		convertRanges(metricConfiguration);
		return metricConfiguration;
	}

	private void convertRanges(MetricConfiguration metricConfiguration) {
		if (ranges != null)
			for (RangeXmlRequest range : ranges)
				metricConfiguration.addRange(range.convert());
	}
}