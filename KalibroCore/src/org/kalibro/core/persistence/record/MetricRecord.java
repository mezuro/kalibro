package org.kalibro.core.persistence.record;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.kalibro.Metric;
import org.kalibro.dto.DataTransferObject;

@MappedSuperclass
public abstract class MetricRecord<METRIC extends Metric> extends DataTransferObject<METRIC> {

	@Column(name = "name", nullable = false)
	protected String name;

	@Column(nullable = false)
	protected String scope;

	@Column
	protected String description;

	public MetricRecord() {
		super();
	}

	public MetricRecord(METRIC metric) {
		name = metric.getName();
		scope = metric.getScope().name();
		description = metric.getDescription();
	}
}