package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.CompoundMetric;

@XmlRootElement(name = "compoundMetric")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompoundMetricXml extends MetricXml<CompoundMetric> {

	@XmlElement(required = true)
	private String script;

	public CompoundMetricXml() {
		super();
	}

	public CompoundMetricXml(CompoundMetric compoundMetric) {
		super(compoundMetric);
		this.script = compoundMetric.getScript();
	}

	@Override
	public CompoundMetric convert() {
		CompoundMetric metric = new CompoundMetric();
		metric.setName(name);
		metric.setScope(scope);
		metric.setDescription(description);
		metric.setScript(script);
		return metric;
	}
}