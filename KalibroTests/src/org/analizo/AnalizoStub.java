package org.analizo;

import static org.kalibro.core.model.enums.Granularity.*;
import static org.kalibro.core.model.enums.Language.*;

import java.io.File;
import java.util.*;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;
import org.yaml.snakeyaml.Yaml;

public class AnalizoStub implements MetricCollector {

	private static Map<String, String> metricNames =
		new Yaml().loadAs(AnalizoStub.class.getResourceAsStream("metric_names.yml"), Map.class);

	private static Map<String, Double> resultsMap =
		new Yaml().loadAs(AnalizoStub.class.getResourceAsStream("hello_world_results.yml"), Map.class);

	private static BaseTool baseTool;
	private static SortedSet<NativeModuleResult> nativeResults;

	public static BaseTool baseTool() {
		if (baseTool == null) {
			baseTool = new BaseTool("Analizo");
			baseTool.setCollectorClass(AnalizoStub.class);
			baseTool.setSupportedMetrics(nativeMetrics());
		}
		return baseTool;
	}

	public static Set<NativeModuleResult> nativeResults() {
		if (nativeResults == null) {
			nativeResults = new TreeSet<NativeModuleResult>();
			nativeResults.add(applicationResult());
			nativeResults.add(classResult());
		}
		return nativeResults;
	}

	public static Set<NativeModuleResult> collectMetrics() {
		return new HashSet<NativeModuleResult>(Arrays.asList(applicationResult(), classResult()));
	}

	public static NativeModuleResult applicationResult() {
		return createResult("null", APPLICATION);
	}

	public static NativeModuleResult classResult() {
		return createResult("HelloWorld", CLASS);
	}

	private static NativeModuleResult createResult(String moduleName, Granularity moduleGranularity) {
		NativeModuleResult classResult = new NativeModuleResult(new Module(moduleGranularity, moduleName));
		for (String code : resultsMap.keySet())
			if (isCompatible(code, moduleGranularity))
				classResult.addMetricResult(metricResult(code));
		return classResult;
	}

	private static boolean isCompatible(String code, Granularity scope) {
		boolean startsWithTotal = code.startsWith("total");
		return (scope == APPLICATION && startsWithTotal) || (scope == CLASS && !startsWithTotal);
	}

	private static NativeMetricResult metricResult(String code) {
		return new NativeMetricResult(nativeMetric(code), resultsMap.get(code));
	}

	public static Set<NativeMetric> nativeMetrics() {
		Set<NativeMetric> nativeMetrics = new HashSet<NativeMetric>();
		for (String code : metricNames.keySet())
			nativeMetrics.add(nativeMetric(code));
		return nativeMetrics;
	}

	public static NativeMetric nativeMetric(String code) {
		Granularity scope = code.startsWith("total") ? APPLICATION : CLASS;
		NativeMetric metric = new NativeMetric(metricNames.get(code), scope, C, CPP, JAVA);
		metric.setOrigin("Analizo");
		return metric;
	}

	@Override
	public BaseTool getBaseTool() {
		BaseTool baseTool = new BaseTool("Analizo");
		baseTool.setCollectorClass(AnalizoStub.class);
		baseTool.setSupportedMetrics(nativeMetrics());
		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) {
		return collectMetrics();
	}
}