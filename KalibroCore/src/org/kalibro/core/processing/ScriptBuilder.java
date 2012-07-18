package org.kalibro.core.processing;

import org.kalibro.core.model.*;

public class ScriptBuilder {

	private StringBuffer script;
	private Configuration configuration;

	private ModuleResult moduleResult;
	private CompoundMetric compoundMetric;

	public ScriptBuilder(Configuration configuration, ModuleResult moduleResult,
		CompoundMetric compoundMetric) {
		this.configuration = configuration;
		this.moduleResult = moduleResult;
		this.compoundMetric = compoundMetric;
	}

	public String buildScript() {
		script = new StringBuffer();
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			if (shouldInclude(metricConfiguration.getMetric()))
				script.append(getScriptFor(metricConfiguration));
		return script.toString();
	}

	public boolean shouldInclude(Metric metric) {
		return moduleResult.hasResultFor(metric) || metric.equals(compoundMetric) && isScopeCompatible(metric);
	}

	private boolean isScopeCompatible(Metric metric) {
		return metric.getScope().ordinal() >= moduleResult.getModule().getGranularity().ordinal();
	}

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

	protected Double getValueFor(Metric metric) {
		return moduleResult.getResultFor(metric).getValue();
	}
}