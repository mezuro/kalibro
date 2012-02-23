package org.kalibro.core.processing;

import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.ModuleResult;

public class ScriptBuilder extends AbstractScriptBuilder {

	private ModuleResult moduleResult;
	private CompoundMetric compoundMetric;

	public ScriptBuilder(Configuration configuration, ModuleResult moduleResult, CompoundMetric compoundMetric) {
		super(configuration);
		this.moduleResult = moduleResult;
		this.compoundMetric = compoundMetric;
	}

	@Override
	public boolean shouldInclude(Metric metric) {
		boolean mayInclude = metric.equals(compoundMetric) || moduleResult.hasResultFor(metric);
		return mayInclude && isScopeCompatible(metric);
	}

	private boolean isScopeCompatible(Metric metric) {
		return metric.getScope().ordinal() >= moduleResult.getModule().getGranularity().ordinal();
	}

	@Override
	protected Double getValueFor(Metric metric) {
		return moduleResult.getResultFor(metric).getValue();
	}
}