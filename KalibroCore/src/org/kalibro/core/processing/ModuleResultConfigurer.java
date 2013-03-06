package org.kalibro.core.processing;

import org.kalibro.*;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

/**
 * Add compound metric results, set grade and add descendant results to the parent of a {@link ModuleResult}.
 * 
 * @author Carlos Morais
 */
class ModuleResultConfigurer {

	private Processing processing;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;

	private ModuleResult moduleResult;
	private Configuration configuration;

	ModuleResultConfigurer(Processing processing, MetricResultDatabaseDao metricResultDao,
		ModuleResultDatabaseDao moduleResultDao) {
		this.processing = processing;
		this.metricResultDao = metricResultDao;
		this.moduleResultDao = moduleResultDao;
	}

	void configure(ModuleResult result, Configuration configurationSnapshot) {
		moduleResult = result;
		configuration = configurationSnapshot;
		configure();
	}

	private void configure() {
		saveCompoundResults();
		updateGrade();
		if (moduleResult.hasParent())
			addResultsToParent();
		else
			processing.setResultsRoot(moduleResult);
	}

	private void saveCompoundResults() {
		CompoundResultCalculator calculator = new CompoundResultCalculator(moduleResult, configuration);
		for (MetricResult compoundResult : calculator.calculateCompoundResults()) {
			moduleResult.addMetricResult(compoundResult);
			metricResultDao.save(compoundResult, moduleResult.getId());
		}
	}

	private void updateGrade() {
		double gradeSum = 0.0;
		double weightSum = 0.0;
		for (MetricResult metricResult : moduleResult.getMetricResults())
			if (metricResult.hasGrade()) {
				Double weight = metricResult.getWeight();
				gradeSum += metricResult.getGrade() * weight;
				weightSum += weight;
			}
		moduleResult.setGrade(gradeSum / weightSum);
		moduleResultDao.save(moduleResult, processing.getId());
	}

	private void addResultsToParent() {
		for (MetricResult metricResult : moduleResult.getMetricResults())
			addResultsToParent(metricResult);
	}

	private void addResultsToParent(MetricResult metricResult) {
		MetricConfiguration snapshot = metricResult.getConfiguration();
		Metric metric = metricResult.getMetric();
		Double value = metricResult.getValue();
		ModuleResult parent = moduleResult.getParent();

		if (! (parent.hasResultFor(metric) || metric.isCompound()))
			metricResultDao.save(new MetricResult(snapshot, Double.NaN), parent.getId());
		if (value != Double.NaN)
			metricResultDao.addDescendantResult(value, parent.getId(), snapshot.getId());
		for (Double descendantValue : metricResult.getDescendantResults())
			metricResultDao.addDescendantResult(descendantValue, parent.getId(), snapshot.getId());
	}
}