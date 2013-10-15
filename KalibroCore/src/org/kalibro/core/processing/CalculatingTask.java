package org.kalibro.core.processing;

import org.kalibro.MetricResult;
import org.kalibro.ModuleResult;

/**
 * Add compound metric results and set grades on {@link ModuleResult}s.
 * 
 * @author Carlos Morais
 */
class CalculatingTask extends ProcessSubtask {

	CalculatingTask(ProcessContext context) {
		super(context);
	}

	@Override
	protected void perform() throws Throwable {
		for (ModuleResult moduleResult : context.moduleResultDao.getResultsOfProcessing(context.processing.getId()))
			configure(moduleResult);
	}

	private void configure(ModuleResult moduleResult) {
		saveCompoundResults(moduleResult);
		updateGrade(moduleResult);
		if (!moduleResult.hasParent())
			context.processing.setResultsRoot(moduleResult);
	}

	private void saveCompoundResults(ModuleResult moduleResult) {
		CompoundResultCalculator calculator = new CompoundResultCalculator(moduleResult, context.configuration);
		for (MetricResult compoundResult : calculator.calculateCompoundResults())
			moduleResult.addMetricResult(context.metricResultDao.save(compoundResult, moduleResult.getId()));
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
		context.moduleResultDao.save(moduleResult, context.processing.getId());
	}
}