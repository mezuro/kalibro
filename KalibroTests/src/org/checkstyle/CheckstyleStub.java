package org.checkstyle;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.yaml.snakeyaml.Yaml;

public class CheckstyleStub implements MetricCollector {

	private static Set<NativeMetric> nativeMetrics;
	private static NativeModuleResult result = initializeResult();

	private static NativeModuleResult initializeResult() {
		nativeMetrics = new HashSet<NativeMetric>();

		Module module = new Module(Granularity.CLASS, "Fibonacci");
		result = new NativeModuleResult(module);
		Map<String, String> resultsMap = loadResults();
		for (String metricName : resultsMap.keySet())
			addMetricResult(CheckstyleMetric.valueOf(metricName), resultsMap.get(metricName));
		return result;
	}

	private static Map<String, String> loadResults() {
		InputStream resource = CheckstyleStub.class.getResourceAsStream("fibonacci_results.yml");
		return new Yaml().loadAs(resource, Map.class);
	}

	private static void addMetricResult(CheckstyleMetric metric, String valueExpression) {
		NativeMetric nativeMetric = new NativeMetric(metric.toString(), Granularity.CLASS, Language.JAVA);
		nativeMetrics.add(nativeMetric);
		Double value = evaluateExpression(valueExpression);
		result.addMetricResult(new NativeMetricResult(nativeMetric, value));
	}

	private static Double evaluateExpression(String expression) {
		String[] values = expression.split("/", 2);
		if (values.length > 1)
			return Double.parseDouble(values[0]) / Double.parseDouble(values[1]);
		return Double.parseDouble(expression);
	}

	public static Set<NativeMetric> nativeMetrics() {
		return nativeMetrics;
	}

	public static NativeModuleResult result() {
		return result;
	}

	public static Set<NativeModuleResult> results() {
		return new HashSet<NativeModuleResult>(Arrays.asList(result));
	}

	@Override
	public Set<NativeMetric> getSupportedMetrics() {
		return nativeMetrics();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) {
		return results();
	}
}