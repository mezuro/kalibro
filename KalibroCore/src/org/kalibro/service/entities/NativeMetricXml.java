package org.kalibro.service.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Language;

@XmlRootElement(name = "NativeMetric")
@XmlAccessorType(XmlAccessType.FIELD)
public class NativeMetricXml extends MetricXml<NativeMetric> {

	@XmlElement(required = true)
	private String origin;

	@XmlElement(name = "language")
	private Collection<Language> languages;

	public NativeMetricXml() {
		super();
	}

	public NativeMetricXml(NativeMetric nativeMetric) {
		super(nativeMetric);
		this.origin = nativeMetric.getOrigin();
		this.languages = nativeMetric.getLanguages();
	}

	@Override
	public NativeMetric convert() {
		Collection<Language> convertedLanguages = languages == null ? new ArrayList<Language>() : languages;
		NativeMetric nativeMetric = new NativeMetric(name, scope, convertedLanguages);
		nativeMetric.setDescription(description == null ? "" : description);
		nativeMetric.setOrigin(origin);
		return nativeMetric;
	}
}