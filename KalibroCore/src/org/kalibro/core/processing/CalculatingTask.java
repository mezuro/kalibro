package org.kalibro.core.processing;

import org.kalibro.Configuration;
import org.kalibro.MetricResult;
import org.kalibro.ModuleResult;
import org.kalibro.Processing;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

/**
 * Add compound metric results and set grades on {@link ModuleResult}s.
 * 
 * @author Carlos Morais
 */
class CalculatingTask extends ProcessSubtask {

	private Processing processing;
	private Configuration configuration;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;

	CalculatingTask(ProcessContext context) {
		super(context);
	}

	@Override
	protected void perform() throws Throwable {
		processing = context.processing();
		configuration = context.configurationDao().snapshotFor(processing.getId());
		moduleResultDao = context.moduleResultDao();
		metricResultDao = context.metricResultDao();
		for (ModuleResult moduleResult : moduleResultDao.getResultsOfProcessing(processing.getId()))
			configure(moduleResult);
	}

	private void configure(ModuleResult moduleResult) {
		saveCompoundResults(moduleResult);
		updateGrade(moduleResult);
		if (!moduleResult.hasParent())
			processing.setResultsRoot(moduleResult);
	}

	private void saveCompoundResults(ModuleResult moduleResult) {
		CompoundResultCalculator calculator = new CompoundResultCalculator(moduleResult, configuration);
		for (MetricResult compoundResult : calculator.calculateCompoundResults())
			moduleResult.addMetricResult(metricResultDao.save(compoundResult, moduleResult.getId()));
	}

	private void updateGrade(ModuleResult moduleResult) {
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
}