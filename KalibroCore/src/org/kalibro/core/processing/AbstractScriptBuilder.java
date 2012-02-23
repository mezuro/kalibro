package org.kalibro.core.processing;

import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricConfiguration;

abstract class AbstractScriptBuilder {

	protected StringBuffer script;
	protected Configuration configuration;

	protected AbstractScriptBuilder(Configuration configuration) {
		this.configuration = configuration;
	}

	public String buildScript() {
		script = new StringBuffer();
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			if (shouldInclude(metricConfiguration.getMetric()))
				script.append(getScriptFor(metricConfiguration));
		return script.toString();
	}

	protected abstract boolean shouldInclude(Metric metric);

	protected String getScriptFor(MetricConfiguration metricConfiguration) {
		Metric metric = metricConfiguration.getMetric();
		String code = metricConfiguration.getCode();
		return metric.isCompound() ? getCompoundScript(metric, code) : getNativeScript(metric, code);
	}

	private String getNativeScript(Metric metric, String code) {
		return "var " + code + " = " + getValueFor(metric) + ";\n";
	}

	private String getCompoundScript(Metric metric, String code) {
		return "function " + code + "(){" + ((CompoundMetric) metric).getScript() + "}\n";
	}

	protected abstract Double getValueFor(Metric metric);
}