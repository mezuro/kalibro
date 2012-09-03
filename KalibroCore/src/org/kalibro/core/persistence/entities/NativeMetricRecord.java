package org.kalibro.core.persistence.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

@Entity(name = "NativeMetric")
@PrimaryKey(columns = {@Column(name = "name"), @Column(name = "origin")})
public class NativeMetricRecord extends MetricRecord<NativeMetric> {

	@ManyToOne(optional = false)
	@JoinColumn(name = "origin", nullable = false, referencedColumnName = "name")
	private BaseToolRecord origin;

	@ElementCollection
	private List<String> languages;

	public NativeMetricRecord() {
		super();
	}

	public NativeMetricRecord(NativeMetric nativeMetric) {
		this(nativeMetric, new BaseToolRecord(nativeMetric.getOrigin()));
	}

	public NativeMetricRecord(NativeMetric nativeMetric, BaseToolRecord origin) {
		super(nativeMetric);
		this.origin = origin;
		initializeLanguages(nativeMetric);
	}

	private void initializeLanguages(NativeMetric nativeMetric) {
		languages = new ArrayList<String>();
		for (Language language : nativeMetric.getLanguages())
			languages.add(language.name());
	}

	@Override
	public NativeMetric convert() {
		NativeMetric nativeMetric = new NativeMetric(name, Granularity.valueOf(scope), convertLanguages());
		nativeMetric.setDescription(description);
		nativeMetric.setOrigin(origin.getName());
		return nativeMetric;
	}

	private List<Language> convertLanguages() {
		List<Language> converted = new ArrayList<Language>();
		for (String language : languages)
			converted.add(Language.valueOf(language));
		return converted;
	}
}