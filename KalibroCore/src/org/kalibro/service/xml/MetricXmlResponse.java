package org.kalibro.service.xml;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.*;
import org.kalibro.dto.MetricDto;

/**
 * XML element for {@link Metric} responses.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricXmlResponse extends MetricDto {

	@XmlElement
	private boolean compound;

	@XmlElement
	private String name;

	@XmlElement
	private Granularity scope;

	@XmlElement
	private String description;

	@XmlElement
	private String script;

	@XmlElement(name = "language")
	private Set<Language> languages;

	public MetricXmlResponse() {
		super();
	}

	public MetricXmlResponse(Metric metric) {
		compound = metric.isCompound();
		name = metric.getName();
		scope = metric.getScope();
		description = metric.getDescription();
		if (metric.isCompound())
			script = ((CompoundMetric) metric).getScript();
		else
			languages = ((NativeMetric) metric).getLanguages();
	}

	@Override
	public boolean compound() {
		return compound;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Granularity scope() {
		return scope;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public String script() {
		return script;
	}

	@Override
	public Set<Language> languages() {
		return languages == null ? new HashSet<Language>() : languages;
	}
}