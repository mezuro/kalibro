package org.kalibro.core.persistence.record;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.model.Metric;

@MappedSuperclass
public abstract class MetricRecord<METRIC extends Metric> implements DataTransferObject<METRIC> {

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