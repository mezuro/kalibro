package org.kalibro.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;

public abstract class CollectorIntegrationTest<T extends MetricCollector> extends IntegrationTest {

	protected T collector;

	private Map<Module, Map<String, String>> expectedResults;

	@Before
	public void setUp() throws Exception {
		Class<T> collectorClass = (Class<T>) Class.forName(getClass().getName().replace("Test", "MetricCollector"));
		collector = collectorClass.newInstance();
	}

	@Test
	public void checkBaseToolInformation() throws IOException {
		assertEquals(expectedName(), collector.name());
		assertEquals(loadResource("description"), collector.description());
		assertDeepEquals(expectedSupportedMetrics(), collector.supportedMetrics());
	}

	protected abstract String expectedName();

	protected abstract Set<? extends NativeMetric> expectedSupportedMetrics();

	@Test
	public void shouldCollectMetrics() throws Exception {
		Producer<NativeModuleResult> producer = new Producer<NativeModuleResult>();

		File codeDirectory = new File(samplesDirectory(), collector.name().toLowerCase());
		Set<NativeMetric> wantedMetrics = collector.supportedMetrics();
		Writer<NativeModuleResult> resultWriter = producer.createWriter();
		collector.collectMetrics(codeDirectory, wantedMetrics, resultWriter);

		expectedResults = loadYaml("expected-results", Map.class);
		for (NativeModuleResult moduleResult : producer)
			verifyModuleResult(moduleResult);
		assertTrue("Did not produce results for modules: " + expectedResults.keySet(), expectedResults.isEmpty());
	}

	private void verifyModuleResult(NativeModuleResult moduleResult) {
		Module module = moduleResult.getModule();
		assertDeepEquals(expectedModule(module), module);

		SortedSet<NativeMetricResult> metricResults = moduleResult.getMetricResults();
		assertEquals("Unexpected number of results for module: " + module.getLongName() + ".",
			expectedResults.get(module).size(), metricResults.size());
		for (NativeMetricResult metricResult : metricResults)
			verifyMetricResult(module, metricResult);

		expectedResults.remove(module);
	}

	private Module expectedModule(Module module) {
		for (Module expected : expectedResults.keySet())
			if (expected.equals(module))
				return expected;
		throw new AssertionError("Produced unexpected result for module: " + module.getLongName() + ".");
	}

	private void verifyMetricResult(Module module, NativeMetricResult metricResult) {
		String metricName = metricResult.getMetric().getName();
		Map<String, String> map = expectedResults.get(module);
		String description = " result for module " + module.getLongName() + " on metric: " + metricName + ".";
		assertTrue("Produced unexpected" + description, map.containsKey(metricName));
		assertDoubleEquals("Wrong" + description, evaluate(map.get(metricName)), metricResult.getValue());
	}

	private Double evaluate(String expression) {
		String[] values = expression.split("/", 2);
		if (values.length > 1)
			return Double.parseDouble(values[0]) / Double.parseDouble(values[1]);
		return Double.parseDouble(expression);
	}
}