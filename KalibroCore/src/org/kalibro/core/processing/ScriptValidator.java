package org.kalibro.core.processing;

import org.kalibro.KalibroException;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricConfiguration;

public class ScriptValidator {

	private Configuration configuration;

	public ScriptValidator(Configuration configuration) {
		this.configuration = configuration;
	}

	public void validateScriptOf(MetricConfiguration metricConfiguration) {
		Metric metric = metricConfiguration.getMetric();
		try {
			doValidate(metricConfiguration);
		} catch (Exception exception) {
			throw new KalibroException("Metric with invalid code or script: " + metric, exception);
		}
	}

	private void doValidate(MetricConfiguration metricConfiguration) {
		String validationScript = new ValidationScriptBuilder(configuration, metricConfiguration).buildScript();
		JavascriptEvaluator.create().compileAndEvaluate(validationScript + metricConfiguration.getCode() + "();");
	}
}