package org.kalibro.core.processing;

import org.kalibro.CompoundMetric;
import org.kalibro.Configuration;
import org.kalibro.Metric;
import org.kalibro.MetricConfiguration;

public final class ScriptValidator {

	public static void validate(Configuration configuration) {
		JavascriptEvaluator evaluator = new JavascriptEvaluator();
		for (MetricConfiguration each : configuration.getMetricConfigurations())
			add(each, evaluator);
		for (MetricConfiguration each : configuration.getMetricConfigurations())
			evaluator.evaluate(each.getCode());
		evaluator.close();
	}

	private static void add(MetricConfiguration configuration, JavascriptEvaluator evaluator) {
		String code = configuration.getCode();
		Metric metric = configuration.getMetric();
		if (metric.isCompound())
			evaluator.addFunction(code, ((CompoundMetric) metric).getScript());
		else
			evaluator.addVariable(code, 1.0);
	}

	private ScriptValidator() {
		return;
	}
}