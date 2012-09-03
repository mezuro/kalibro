package org.kalibro.core.persistence.entities;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.enums.Granularity;

@Entity(name = "CompoundMetric")
@PrimaryKey(columns = {@Column(name = "name"), @Column(name = "configuration")})
public class CompoundMetricRecord extends MetricRecord<CompoundMetric> {

	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "configuration", nullable = false, referencedColumnName = "configuration"),
		@JoinColumn(insertable = false, name = "name", referencedColumnName = "metricName", updatable = false)})
	@SuppressWarnings("all" /* used by JPA */)
	private MetricConfigurationRecord metricConfiguration;

	@Column(nullable = false)
	private String script;

	public CompoundMetricRecord() {
		super();
	}

	public CompoundMetricRecord(CompoundMetric compoundMetric, MetricConfigurationRecord metricConfiguration) {
		super(compoundMetric);
		this.metricConfiguration = metricConfiguration;
		this.script = compoundMetric.getScript();
	}

	@Override
	public CompoundMetric convert() {
		CompoundMetric metric = new CompoundMetric();
		metric.setName(name);
		metric.setScope(Granularity.valueOf(scope));
		metric.setDescription(description);
		metric.setScript(script);
		return metric;
	}
}