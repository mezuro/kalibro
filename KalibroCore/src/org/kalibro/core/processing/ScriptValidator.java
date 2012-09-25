package org.kalibro.core.processing;

import org.kalibro.CompoundMetric;
import org.kalibro.KalibroException;
import org.kalibro.Metric;
import org.kalibro.MetricConfiguration;

public class ScriptValidator {

	private JavascriptEvaluator evaluator;

	public ScriptValidator() {
		evaluator = new JavascriptEvaluator();
	}

	public void add(MetricConfiguration configuration) {
		try {
			doAdd(configuration);
		} catch (Exception exception) {
			Metric metric = configuration.getMetric();
			throw new KalibroException("Metric with invalid code or script: " + metric, exception);
		}
	}

	public void remove(MetricConfiguration configuration) {
		evaluator.remove(configuration.getCode());
	}

	private void doAdd(MetricConfiguration configuration) {
		String code = configuration.getCode();
		Metric metric = configuration.getMetric();
		if (metric.isCompound())
			evaluator.addFunction(code, ((CompoundMetric) metric).getScript());
		else
			evaluator.addVariable(code, 1.0);
		evaluator.evaluate(code);
	}
}