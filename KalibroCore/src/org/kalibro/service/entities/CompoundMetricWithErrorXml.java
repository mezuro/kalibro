package org.kalibro.service.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.CompoundMetric;

@XmlRootElement(name = "CompoundMetricWithError")
@XmlAccessorType(XmlAccessType.FIELD)
class CompoundMetricWithErrorXml {

	private CompoundMetricXml metric;
	private ErrorXml error;

	public CompoundMetricWithErrorXml() {
		super();
	}

	protected CompoundMetricWithErrorXml(CompoundMetric metric, Throwable error) {
		this.metric = new CompoundMetricXml(metric);
		this.error = new ErrorXml(error);
	}

	protected CompoundMetric getMetric() {
		return metric.convert();
	}

	protected Throwable getError() {
		return error.convert();
	}
}