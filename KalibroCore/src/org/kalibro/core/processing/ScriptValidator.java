package org.kalibro.core.processing;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;

public class ScriptValidator {

	private Configuration configuration;

	public ScriptValidator(Configuration configuration) {
		this.configuration = configuration;
	}

	public void validateScriptOf(MetricConfiguration metricConfiguration) {
		if (metricConfiguration.getMetric().isCompound()) {
			String validationScript = new ValidationScriptBuilder(configuration, metricConfiguration).buildScript();
			new ScriptEvaluator(validationScript).invokeFunction(metricConfiguration.getCode());
		}
	}
}