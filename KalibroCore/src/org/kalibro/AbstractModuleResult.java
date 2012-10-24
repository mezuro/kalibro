package org.kalibro;

import java.util.Set;
import java.util.SortedSet;
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

	private Set<METRIC_RESULT> metricResults;

	AbstractModuleResult(Module module) {
		this.module = module;
		setMetricResults(new TreeSet<METRIC_RESULT>());
	}

	public final Module getModule() {
		return module;
	}

	public final boolean hasResultFor(Metric metric) {
		return findResultFor(metric) != null;
	}

	public final METRIC_RESULT getResultFor(Metric metric) {
		METRIC_RESULT metricResult = findResultFor(metric);
		throwExceptionIf(metricResult == null, "No result found for metric: " + metric);
		return metricResult;
	}

	private METRIC_RESULT findResultFor(Metric metric) {
		for (METRIC_RESULT metricResult : metricResults)
			if (metricResult.getMetric().equals(metric))
				return metricResult;
		return null;
	}

	public final SortedSet<METRIC_RESULT> getMetricResults() {
		return new TreeSet<METRIC_RESULT>(metricResults);
	}

	public final void setMetricResults(SortedSet<METRIC_RESULT> metricResults) {
		this.metricResults = metricResults;
	}

	public final void addMetricResult(METRIC_RESULT metricResult) {
		if (metricResults.contains(metricResult))
			metricResults.remove(metricResult);
		metricResults.add(metricResult);
	}
}