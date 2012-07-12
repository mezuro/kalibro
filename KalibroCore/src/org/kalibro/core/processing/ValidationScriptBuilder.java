package org.kalibro.core.processing;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricConfiguration;

class ValidationScriptBuilder extends AbstractScriptBuilder {

	private MetricConfiguration toValidate;

	protected ValidationScriptBuilder(Configuration configuration, MetricConfiguration toValidate) {
		super(configuration);
		this.toValidate = toValidate;
	}

	@Override
	public String buildScript() {
		if (toValidate.getMetric().isCompound())
			return super.buildScript() + getScriptFor(toValidate);
		return "function " + toValidate.getCode() + "(){return 1.0;}";
	}

	@Override
	protected boolean shouldInclude(Metric metric) {
		return ! metric.isCompound();
	}

	@Override
	protected Double getValueFor(Metric metric) {
		return 1.0;
	}
}