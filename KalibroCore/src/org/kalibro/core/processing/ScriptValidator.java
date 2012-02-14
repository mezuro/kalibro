package org.kalibro.core.processing;

import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Configuration;

public class ScriptValidator {

	private Configuration configuration;

	public ScriptValidator(Configuration configuration) {
		this.configuration = configuration;
	}

	public void validateScriptOf(CompoundMetric metric) throws Exception {
		String code = configuration.getConfigurationFor(metric).getCode();
		String validationScript = new ValidationScriptBuilder(configuration, metric).buildScript();
		new ScriptEvaluator(validationScript).invokeFunction(code);
	}
}