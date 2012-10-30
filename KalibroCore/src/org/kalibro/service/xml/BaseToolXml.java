package org.kalibro.service.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.BaseTool;
import org.kalibro.Metric;
import org.kalibro.NativeMetric;
import org.kalibro.dto.BaseToolDto;

/**
 * XML element for {@link BaseTool}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "baseTool")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseToolXml extends BaseToolDto {

	@XmlElement
	private String name;

	@XmlElement
	private String description;

	@XmlElement
	private String collectorClassName;

	@XmlElement(name = "supportedMetric")
	private Collection<MetricXml> supportedMetrics;

	public BaseToolXml() {
		super();
	}

	public BaseToolXml(BaseTool baseTool) {
		name = baseTool.getName();
		description = baseTool.getDescription();
		collectorClassName = baseTool.getCollectorClassName();
		supportedMetrics = createDtos(new ArrayList<Metric>(baseTool.getSupportedMetrics()), MetricXml.class);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public String collectorClassName() {
		return collectorClassName;
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		HashSet<NativeMetric> converted = new HashSet<NativeMetric>();
		if (supportedMetrics != null)
			for (MetricXml supportedMetric : supportedMetrics)
				converted.add((NativeMetric) supportedMetric.convert());
		return converted;
	}
}