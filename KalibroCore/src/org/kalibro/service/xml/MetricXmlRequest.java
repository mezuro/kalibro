package org.kalibro.service.xml;

import java.util.Set;

import org.kalibro.*;
import org.kalibro.dto.MetricDto;

public class MetricXmlRequest extends MetricDto {

	private boolean compound;
	private String name;
	private Granularity scope;
	private String description;
	private Set<Language> languages;
	private String script;

	public MetricXmlRequest() {
		super();
	}

	public MetricXmlRequest(Metric metric) {
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
	public Set<Language> languages() {
		return languages;
	}

	@Override
	public String script() {
		return script;
	}
}