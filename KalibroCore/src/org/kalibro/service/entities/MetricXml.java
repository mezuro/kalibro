package org.kalibro.service.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.kalibro.core.model.Metric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.dto.DataTransferObject;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({CompoundMetricXml.class, NativeMetricXml.class})
public abstract class MetricXml<METRIC extends Metric> extends DataTransferObject<METRIC> {

	@XmlElement(required = true)
	protected String name;

	@XmlElement(required = true)
	protected Granularity scope;

	protected String description;

	public MetricXml() {
		super();
	}

	public MetricXml(METRIC metric) {
		name = metric.getName();
		scope = metric.getScope();
		description = metric.getDescription();
	}
}