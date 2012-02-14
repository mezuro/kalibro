package org.kalibro.core.model;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;

@SortingMethods("getMetric")
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