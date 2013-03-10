package org.kalibro.core.processing;

import java.util.List;

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
	private Configuration configuration;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;

	private ModuleResult moduleResult;

	ModuleResultConfigurer(Processing processing, Configuration configuration, MetricResultDatabaseDao metricResultDao,
		ModuleResultDatabaseDao moduleResultDao) {
		this.processing = processing;
		this.configuration = configuration;
		this.metricResultDao = metricResultDao;
		this.moduleResultDao = moduleResultDao;
	}

	void configure(ModuleResult result) {
		moduleResult = result;
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
		for (MetricResult compoundResult : calculator.calculateCompoundResults())
			moduleResult.addMetricResult(metricResultDao.save(compoundResult, moduleResult.getId()));
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
		Long parentId = moduleResult.getParent().getId();
		Double value = metricResult.getValue();

		if (!snapshot.getMetric().isCompound())
			makeSureParentHasResultForMetric(parentId, snapshot);
		List<Double> descendantResults = metricResultDao.descendantResultsOf(moduleResult.getId(), snapshot.getId());
		if (!value.isNaN())
			descendantResults.add(value);
		metricResultDao.addDescendantResults(descendantResults, parentId, snapshot.getId());
	}

	private void makeSureParentHasResultForMetric(Long parentId, MetricConfiguration snapshot) {
		if (!metricResultDao.metricResultExists(parentId, snapshot.getId()))
			metricResultDao.save(new MetricResult(snapshot, Double.NaN), parentId);
	}
}