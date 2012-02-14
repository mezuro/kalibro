package org.kalibro.service.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.util.DataTransferObject;

@XmlRootElement(name = "BaseTool")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseToolXml implements DataTransferObject<BaseTool> {

	private String name;
	private String description;

	@XmlElement(name = "supportedMetric")
	private List<NativeMetricXml> supportedMetrics;

	public BaseToolXml() {
		super();
	}

	public BaseToolXml(BaseTool baseTool) {
		name = baseTool.getName();
		description = baseTool.getDescription();
		initializeSupportedMetrics(baseTool);
	}

	private void initializeSupportedMetrics(BaseTool baseTool) {
		supportedMetrics = new ArrayList<NativeMetricXml>();
		for (NativeMetric supportedMetric : baseTool.getSupportedMetrics())
			supportedMetrics.add(new NativeMetricXml(supportedMetric));
	}

	@Override
	public BaseTool convert() {
		BaseTool baseTool = new BaseTool(name);
		baseTool.setDescription(description);
		convertSupportedMetrics(baseTool);
		return baseTool;
	}

	private void convertSupportedMetrics(BaseTool baseTool) {
		Set<NativeMetric> metrics = new TreeSet<NativeMetric>();
		for (NativeMetricXml supportedMetric : supportedMetrics)
			metrics.add(supportedMetric.convert());
		baseTool.setSupportedMetrics(metrics);
	}
}