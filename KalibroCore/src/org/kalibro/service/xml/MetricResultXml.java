package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.dto.MetricResultDto;

/**
 * XML element for {@link MetricResult}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "metricResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricResultXml extends MetricResultDto {

	@XmlElement
	private Long id;

	@XmlElement
	private MetricConfigurationSnapshotXml configuration;

	@XmlElement
	private Double value;

	@XmlElement
	private ThrowableXml error;

	public MetricResultXml() {
		super();
	}

	public MetricResultXml(MetricResult metricResult) {
		id = metricResult.getId();
		configuration = new MetricConfigurationSnapshotXml(metricResult.getConfiguration());
		value = metricResult.getValue();
		if (metricResult.hasError())
			error = new ThrowableXml(metricResult.getError());
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public MetricConfiguration configuration() {
		return configuration.convert();
	}

	@Override
	public Double value() {
		return value;
	}

	@Override
	public Throwable error() {
		return error == null ? null : error.convert();
	}
}