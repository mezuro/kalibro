package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.BaseTool;
import org.kalibro.Metric;
import org.kalibro.MetricConfiguration;
import org.kalibro.Statistic;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.dto.MetricConfigurationDto;

/**
 * XML element for {@link MetricConfiguration} responses.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "metricConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricConfigurationXmlResponse extends MetricConfigurationDto {

	@XmlElement
	private Long id;

	@XmlElement
	private String code;

	@XmlElement
	private MetricXmlResponse metric;

	@XmlElement
	private String baseToolName;

	@XmlElement
	private Double weight;

	@XmlElement
	private Statistic aggregationForm;

	public MetricConfigurationXmlResponse() {
		super();
	}

	public MetricConfigurationXmlResponse(MetricConfiguration metricConfiguration) {
		id = metricConfiguration.getId();
		code = metricConfiguration.getCode();
		metric = new MetricXmlResponse(metricConfiguration.getMetric());
		if (!metric.compound())
			baseToolName = metricConfiguration.getBaseTool().getName();
		weight = metricConfiguration.getWeight();
		aggregationForm = metricConfiguration.getAggregationForm();
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
	public BaseTool baseTool() {
		return metric.compound() ? null : (BaseTool) DaoLazyLoader.createProxy(BaseToolDao.class, "get", baseToolName);
	}

	@Override
	public Double weight() {
		return weight;
	}

	@Override
	public Statistic aggregationForm() {
		return aggregationForm;
	}
}