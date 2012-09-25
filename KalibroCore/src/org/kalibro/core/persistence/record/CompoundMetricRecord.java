package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.CompoundMetric;
import org.kalibro.Granularity;

@Entity(name = "CompoundMetric")
@Table(name = "\"COMPOUND_METRIC\"")
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

	public CompoundMetricRecord(CompoundMetric compoundMetric) {
		this(compoundMetric, null);
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