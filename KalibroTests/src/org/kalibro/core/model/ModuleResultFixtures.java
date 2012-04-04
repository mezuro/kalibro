package org.kalibro.core.model;

import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.analizo.AnalizoStub;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.processing.ResultsAggregator;

public final class ModuleResultFixtures {

	public static Collection<ModuleResult> helloWorldModuleResults() {
		return helloWorldModuleResults(new Date());
	}

	public static Collection<ModuleResult> helloWorldModuleResults(Date date) {
		ProjectResult projectResult = helloWorldResult(date);
		ModuleResult applicationResult = helloWorldApplicationResult(date);
		ModuleResult classResult = helloWorldClassResult(date);

		Map<Module, ModuleResult> moduleResultMap = new TreeMap<Module, ModuleResult>();
		moduleResultMap.put(helloWorldApplication(), applicationResult);
		moduleResultMap.put(helloWorldClass(), classResult);
		new ResultsAggregator(projectResult, moduleResultMap).aggregate();

		return moduleResultMap.values();
	}

	public static ModuleResult helloWorldApplicationResult() {
		return helloWorldApplicationResult(new Date());
	}

	public static ModuleResult helloWorldApplicationResult(Date date) {
		ModuleResult result = new ModuleResult(helloWorldApplication(), date);
		result.addMetricResults(AnalizoStub.applicationResult().getMetricResults());
		return result;
	}

	public static ModuleResult helloWorldClassResult() {
		return helloWorldClassResult(new Date());
	}

	public static ModuleResult helloWorldClassResult(Date date) {
		ModuleResult result = new ModuleResult(helloWorldClass(), date);
		result.addMetricResults(AnalizoStub.classResult().getMetricResults());
		return result;
	}

	public static Map<Module, ModuleResult> junitAnalizoResultMap() {
		Map<Module, ModuleResult> resultMap = new TreeMap<Module, ModuleResult>();
		putResult(junitAnalizoTree(), resultMap);
		putResult(analizoNode(), resultMap);
		putResult(analizoMetricCollectorNode(), resultMap);
		putResult(analizoResultParserNode(), resultMap);
		putResult(assertNode(), resultMap);
		putResult(comparisonFailureNode(), resultMap);
		return resultMap;
	}

	private static void putResult(ModuleNode moduleNode, Map<Module, ModuleResult> resultMap) {
		Module module = moduleNode.getModule();
		Granularity scope = module.getGranularity();
		NativeMetric metric = new NativeMetric(scope + "_name_length", scope, Language.JAVA);
		Double value = (double) module.getShortName().length();
		MetricResult metricResult = new MetricResult(metric, value);
		ModuleResult moduleResult = new ModuleResult(module, new Date());
		moduleResult.addMetricResult(metricResult);
		resultMap.put(module, moduleResult);
	}

	private ModuleResultFixtures() {
		return;
	}
}