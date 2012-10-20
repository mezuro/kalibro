package org.checkstyle;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import org.kalibro.*;
import org.kalibro.core.concurrent.Writer;
import org.yaml.snakeyaml.Yaml;

public class CheckstyleStub implements MetricCollector {

	private static Set<NativeMetric> nativeMetrics;
	private static NativeModuleResult result = initializeResult();

	private static NativeModuleResult initializeResult() {
		nativeMetrics = new TreeSet<NativeMetric>();

		Module module = new Module(Granularity.CLASS, "org", "fibonacci", "Fibonacci");
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
	public String name() {
		return "Checkstyle";
	}

	@Override
	public String description() {
		return "";
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return nativeMetrics;
	}

	@Override
	public void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		for (NativeModuleResult moduleResult : results())
			resultWriter.write(moduleResult);
		resultWriter.close();
	}
}