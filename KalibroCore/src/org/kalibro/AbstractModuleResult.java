package org.kalibro;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

@SortingFields("module")
public abstract class AbstractModuleResult<METRIC_RESULT extends AbstractMetricResult>
	extends AbstractEntity<AbstractModuleResult<METRIC_RESULT>> {

	@IdentityField
	protected Module module;

	protected Map<Metric, METRIC_RESULT> metricResults;

	public AbstractModuleResult(Module module) {
		this.module = module;
		metricResults = new TreeMap<Metric, METRIC_RESULT>();
	}

	public Module getModule() {
		return module;
	}

	public Collection<METRIC_RESULT> getMetricResults() {
		return metricResults.values();
	}

	public boolean hasResultFor(Metric metric) {
		return metricResults.containsKey(metric);
	}

	public METRIC_RESULT getResultFor(Metric metric) {
		if (!hasResultFor(metric))
			throw new KalibroException("No result found for metric: " + metric);
		return metricResults.get(metric);
	}

	public void addMetricResult(METRIC_RESULT metricResult) {
		metricResults.put(metricResult.getMetric(), metricResult);
	}
}