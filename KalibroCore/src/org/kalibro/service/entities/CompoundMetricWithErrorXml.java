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

	public CompoundMetricWithErrorXml(CompoundMetric metric, Exception error) {
		this.metric = new CompoundMetricXml(metric);
		this.error = new ErrorXml(error);
	}

	public CompoundMetric getMetric() {
		return metric.convert();
	}

	public Exception getError() {
		return error.convert();
	}
}