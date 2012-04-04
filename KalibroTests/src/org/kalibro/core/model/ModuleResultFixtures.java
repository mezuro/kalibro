package org.kalibro.core.model;

import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.processing.ResultsAggregator;

public final class ModuleResultFixtures {

	private static ModuleResult helloWorldApplicationResult = newHelloWorldApplicationResult();
	private static ModuleResult helloWorldClassResult = newHelloWorldClassResult();

	public static ModuleResult helloWorldApplicationResult() {
		return helloWorldApplicationResult;
	}

	public static ModuleResult newHelloWorldApplicationResult() {
		return newHelloWorldApplicationResult(new Date());
	}

	public static ModuleResult newHelloWorldApplicationResult(Date date) {
		ModuleResult result = new ModuleResult(newHelloWorldApplication(), date);
		result.addMetricResults(NativeModuleResultFixtures.helloWorldApplicationResult().getMetricResults());
		return result;
	}

	public static ModuleResult helloWorldClassResult() {
		return helloWorldClassResult;
	}

	public static ModuleResult newHelloWorldClassResult() {
		return newHelloWorldClassResult(new Date());
	}

	public static ModuleResult newHelloWorldClassResult(Date date) {
		ModuleResult result = new ModuleResult(newHelloWorldClass(), date);
		result.addMetricResults(NativeModuleResultFixtures.helloWorldClassResult().getMetricResults());
		return result;
	}

	public static Collection<ModuleResult> newHelloWorldResults() {
		return newHelloWorldResults(new Date());
	}

	public static Collection<ModuleResult> newHelloWorldResults(Date date) {
		Map<Module, ModuleResult> moduleResultMap = new TreeMap<Module, ModuleResult>();
		moduleResultMap.put(newHelloWorldApplication(), newHelloWorldApplicationResult(date));
		moduleResultMap.put(newHelloWorldClass(), newHelloWorldClassResult(date));
		new ResultsAggregator(newHelloWorldResult(date), moduleResultMap).aggregate();
		return moduleResultMap.values();
	}

	public static Map<Module, ModuleResult> analizoCheckstyleResultMap() {
		Map<Module, ModuleResult> resultMap = new TreeMap<Module, ModuleResult>();
		putResult(analizoNode(), resultMap);
		putResult(analizoMetricCollectorNode(), resultMap);
		putResult(analizoOutputParserNode(), resultMap);
		putResult(checkstyleMetricCollectorNode(), resultMap);
		putResult(checkstyleOutputParserNode(), resultMap);
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