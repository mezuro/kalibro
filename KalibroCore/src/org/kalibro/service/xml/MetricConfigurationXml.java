package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Metric;
import org.kalibro.MetricConfiguration;
import org.kalibro.Statistic;
import org.kalibro.dto.MetricConfigurationDto;

/**
 * XML element for {@link MetricConfiguration}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "metricConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricConfigurationXml extends MetricConfigurationDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String code;

	@XmlElement(required = true)
	private MetricXml metric;

	@XmlElement
	private String baseToolName;

	@XmlElement(required = true)
	private Double weight;

	@XmlElement(required = true)
	private Statistic aggregationForm;

	@XmlElement
	private Long readingGroupId;

	public MetricConfigurationXml() {
		super();
	}

	public MetricConfigurationXml(MetricConfiguration metricConfiguration) {
		id = metricConfiguration.getId();
		code = metricConfiguration.getCode();
		metric = new MetricXml(metricConfiguration.getMetric());
		if (!metric.compound())
			baseToolName = metricConfiguration.getBaseTool().getName();
		weight = metricConfiguration.getWeight();
		aggregationForm = metricConfiguration.getAggregationForm();
		readingGroupId = metricConfiguration.hasReadingGroup() ? metricConfiguration.getReadingGroup().getId() : null;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String code() {
		return code;
	}

	@Override
	public Metric metric() {
		return metric.convert();
	}

	@Override
	public String baseToolName() {
		return baseToolName;
	}

	@Override
	public Double weight() {
		return weight;
	}

	@Override
	public Statistic aggregationForm() {
		return aggregationForm;
	}

	@Override
	public Long readingGroupId() {
		return readingGroupId;
	}
}