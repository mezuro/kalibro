package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.CompoundMetric;

@XmlRootElement(name = "CompoundMetricWithError")
@XmlAccessorType(XmlAccessType.FIELD)
class CompoundMetricWithErrorXml {

	private CompoundMetricXml metric;
	private ThrowableXml error;

	public CompoundMetricWithErrorXml() {
		super();
	}

	protected CompoundMetricWithErrorXml(CompoundMetric metric, Throwable error) {
		this.metric = new CompoundMetricXml(metric);
		this.error = new ThrowableXml(error);
	}

	protected CompoundMetric getMetric() {
		return metric.convert();
	}

	protected Throwable getError() {
		return error.convert();
	}
}