package org.kalibro.service.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.BaseTool;
import org.kalibro.NativeMetric;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "baseTool")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseToolXml extends DataTransferObject<BaseTool> {

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
		return new BaseTool(name, description, toSet(supportedMetrics));
	}
}