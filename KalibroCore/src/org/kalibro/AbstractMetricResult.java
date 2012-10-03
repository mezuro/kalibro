package org.kalibro;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * Abstract representation of metric results.
 * 
 * @author Carlos Morais.
 */
@SortingFields("metric")
abstract class AbstractMetricResult extends AbstractEntity<AbstractMetricResult> {

	@IdentityField
	private Metric metric;

	private Double value;

	AbstractMetricResult(Metric metric, Double value) {
		this.metric = metric;
		setValue(value);
	}

	public final Metric getMetric() {
		return metric;
	}

	public final Double getValue() {
		return value;
	}

	protected final void setValue(Double value) {
		this.value = value;
	}
}