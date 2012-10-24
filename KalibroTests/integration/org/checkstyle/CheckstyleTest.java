package org.checkstyle;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.CLASS;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.IntegrationTest;

public class CheckstyleTest extends IntegrationTest {

	private Set<NativeMetric> wantedMetrics;
	private Map<String, String> expectedResults;

	private CheckstyleMetricCollector checkstyle;

	@Before
	public void setUp() throws IOException {
		checkstyle = new CheckstyleMetricCollector();
		wantedMetrics = checkstyle.supportedMetrics();
		expectedResults = loadYaml("result-Fibonacci", Map.class);
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		Producer<NativeModuleResult> producer = new Producer<NativeModuleResult>();

		File codeDirectory = new File(samplesDirectory(), "checkstyle");
		Writer<NativeModuleResult> resultWriter = producer.createWriter();
		checkstyle.collectMetrics(codeDirectory, wantedMetrics, resultWriter);

		Iterator<NativeModuleResult> iterator = producer.iterator();
		verifyExpectedResult(iterator.next());
		assertFalse(iterator.hasNext());
	}

	private void verifyExpectedResult(NativeModuleResult moduleResult) {
		assertDeepEquals(new Module(CLASS, "org", "fibonacci", "Fibonacci.java"), moduleResult.getModule());

		assertEquals(wantedMetrics.size(), moduleResult.getMetricResults().size());
		for (Metric metric : wantedMetrics) {
			NativeMetricResult metricResult = moduleResult.getResultFor(metric);
			assertDeepEquals(metric, metricResult.getMetric());
			assertDoubleEquals("Wrong result for " + metric + ".", expectedResultFor(metric), metricResult.getValue());
		}
	}

	private Double expectedResultFor(Metric metric) {
		String expression = expectedResults.get(metric.getName());
		String[] values = expression.split("/", 2);
		if (values.length > 1)
			return Double.parseDouble(values[0]) / Double.parseDouble(values[1]);
		return Double.parseDouble(expression);
	}
}