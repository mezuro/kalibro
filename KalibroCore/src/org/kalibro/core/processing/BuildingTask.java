package org.kalibro.core.processing;

import static org.kalibro.Granularity.*;

import java.util.ArrayList;
import java.util.List;

import org.kalibro.*;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

/**
 * Consumes {@link NativeModuleResult}s produced by collectors and builds the source tree of {@link ModuleResult}s.
 * 
 * @author Carlos Morais
 */
class BuildingTask extends ProcessSubtask {

	private Long processingId;
	private Configuration configuration;
	private ModuleResultDatabaseDao moduleResultDao;
	private MetricResultDatabaseDao metricResultDao;

	BuildingTask(ProcessContext context) {
		super(context);
	}

	@Override
	protected void perform() throws Throwable {
		processingId = context.processing().getId();
		configuration = context.configuration();
		moduleResultDao = context.moduleResultDao();
		metricResultDao = context.metricResultDao();
		for (NativeModuleResult nativeModuleResult : context.resultProducer())
			addNativeResult(nativeModuleResult);
	}

	private void addNativeResult(NativeModuleResult nativeResult) {
		Long moduleResultId = save(nativeResult.getModule());
		List<MetricResult> metricResults = new ArrayList<MetricResult>();
		for (NativeMetricResult metricResult : nativeResult.getMetricResults())
			metricResults.add(configureMetricResult(metricResult));
		metricResultDao.saveAll(metricResults, moduleResultId);
	}

	private Long save(Module nativeModule) {
		return getResultFor(nativeModule).getId();
	}

	private ModuleResult getResultFor(Module module) {
		if (module == null)
			return null;
		ModuleResult moduleResult = moduleResultDao.getResultFor(module, processingId);
		if (moduleResult == null) {
			ModuleResult parent = getResultFor(module.inferParent());
			moduleResult = moduleResultDao.save(newResult(parent, module), processingId);
		} else if (moduleResult.getModule().getGranularity() != module.getGranularity()) {
			moduleResult.getModule().setGranularity(module.getGranularity());
			moduleResult = moduleResultDao.save(moduleResult, processingId);
		}
		return moduleResult;
	}

	private ModuleResult newResult(ModuleResult parent, Module module) {
		if (module.getGranularity() == SOFTWARE)
			return new ModuleResult(null, new Module(SOFTWARE, context.repository().getName()));
		return new ModuleResult(parent, module);
	}

	private MetricResult configureMetricResult(NativeMetricResult nativeMetricResult) {
		Metric metric = nativeMetricResult.getMetric();
		Double value = nativeMetricResult.getValue();
		MetricConfiguration snapshot = configuration.getConfigurationFor(metric);
		return new MetricResult(snapshot, value);
	}
}