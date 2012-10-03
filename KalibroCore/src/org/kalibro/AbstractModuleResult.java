package org.kalibro;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * Abstract representation of a set of metric results collected from the same instance of a {@link Module}.
 * 
 * @author Carlos Morais
 */
@SortingFields("module")
abstract class AbstractModuleResult<METRIC_RESULT extends AbstractMetricResult>
	extends AbstractEntity<AbstractModuleResult<METRIC_RESULT>> {

	@IdentityField
	private Module module;

	private Map<Metric, METRIC_RESULT> metricResults;

	AbstractModuleResult(Module module) {
		this.module = module;
		metricResults = new TreeMap<Metric, METRIC_RESULT>();
	}

	public final Module getModule() {
		return module;
	}

	public final boolean hasResultFor(Metric metric) {
		return metricResults.containsKey(metric);
	}

	public final METRIC_RESULT getResultFor(Metric metric) {
		if (!hasResultFor(metric))
			throw new KalibroException("No result found for metric: " + metric);
		return metricResults.get(metric);
	}

	public final SortedSet<Metric> getMetrics() {
		return new TreeSet<Metric>(metricResults.keySet());
	}

	public final SortedSet<METRIC_RESULT> getMetricResults() {
		return new TreeSet<METRIC_RESULT>(metricResults.values());
	}

	public final void addMetricResult(METRIC_RESULT metricResult) {
		metricResults.put(metricResult.getMetric(), metricResult);
	}

	public final void removeResultFor(Metric metric) {
		metricResults.remove(metric);
	}
}