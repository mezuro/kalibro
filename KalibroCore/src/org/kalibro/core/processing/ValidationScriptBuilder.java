package org.kalibro.core.processing;

import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricConfiguration;

class ValidationScriptBuilder extends AbstractScriptBuilder {

	private CompoundMetric validatonMetric;

	protected ValidationScriptBuilder(Configuration configuration, CompoundMetric validationMetric) {
		super(configuration);
		this.validatonMetric = validationMetric;
	}

	@Override
	public String buildScript() {
		MetricConfiguration compoundMetric = configuration.getConfigurationFor(validatonMetric);
		return super.buildScript() + getScriptFor(compoundMetric);
	}

	@Override
	protected boolean shouldInclude(Metric metric) {
		return !metric.isCompound();
	}

	@Override
	protected Double getValueFor(Metric metric) {
		return 1.0;
	}
}