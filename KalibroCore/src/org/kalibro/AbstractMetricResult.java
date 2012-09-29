package org.kalibro;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

@SortingFields("metric")
public abstract class AbstractMetricResult extends AbstractEntity<AbstractMetricResult> {

	@IdentityField
	protected Metric metric;

	protected Double value;

	public AbstractMetricResult(Metric metric, Double value) {
		this.metric = metric;
		this.value = value;
	}

	public Metric getMetric() {
		return metric;
	}

	public Double getValue() {
		return value;
	}
}