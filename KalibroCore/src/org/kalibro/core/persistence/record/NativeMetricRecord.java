package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.Granularity;
import org.kalibro.Language;
import org.kalibro.NativeMetric;

@Entity(name = "NativeMetric")
@Table(name = "\"NATIVE_METRIC\"")
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
		return nativeMetric;
	}

	private Language[] convertLanguages() {
		Language[] convertedLanguages = new Language[0];
		return languages == null ? convertedLanguages : languages.toArray(convertedLanguages);
	}
}