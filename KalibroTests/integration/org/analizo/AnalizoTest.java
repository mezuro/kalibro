package org.analizo;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.*;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.IntegrationTest;

public class AnalizoTest extends IntegrationTest {

	@BeforeClass
	public static void checkAnalizoVersion() {
		try {
			String message = "The Analizo version installed is not the expected for this test.";
			assertEquals(message, "analizo version 1.16.0", getAnalizoVersion());
		} catch (IOException exception) {
			fail("Analizo is not installed but is required for this test.");
		}
	}

	private static String getAnalizoVersion() throws IOException {
		InputStream output = new CommandTask("analizo --version").executeAndGetOuput();
		return IOUtils.toString(output).trim();
	}

	private Set<NativeMetric> wantedMetrics;
	private Map<String, Map<String, String>> expectedResults;

	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws IOException {
		analizo = new AnalizoMetricCollector();
		wantedMetrics = analizo.supportedMetrics();
		expectedResults = loadYaml("result-HelloWorld", Map.class);
	}

	@Test
	public void shouldGetSupportedMetrics() {
		assertDeepEquals(loadYaml("supported-metrics", Set.class), analizo.supportedMetrics());
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		Producer<NativeModuleResult> producer = new Producer<NativeModuleResult>();

		File codeDirectory = new File(samplesDirectory(), "analizo");
		Writer<NativeModuleResult> resultWriter = producer.createWriter();
		analizo.collectMetrics(codeDirectory, wantedMetrics, resultWriter);

		Iterator<NativeModuleResult> iterator = producer.iterator();
		verifyExpectedResult(SOFTWARE, iterator.next());
		verifyExpectedResult(CLASS, iterator.next());
		assertFalse(iterator.hasNext());
	}

	private void verifyExpectedResult(Granularity granularity, NativeModuleResult moduleResult) {
		Module expectedModule = granularity == CLASS ? new Module(CLASS, "HelloWorld") : new Module(SOFTWARE, "null");
		assertDeepEquals(expectedModule, moduleResult.getModule());

		assertEquals(expectedResults.get("" + granularity).size(), moduleResult.getMetricResults().size());
		for (Metric metric : wantedMetrics)
			if (metric.getScope() == granularity) {
				NativeMetricResult metricResult = moduleResult.getResultFor(metric);
				assertDeepEquals(metric, metricResult.getMetric());

				Double expectedResult = expectedResultFor(granularity, metric);
				assertDoubleEquals("Wrong result for " + metric + ".", expectedResult, metricResult.getValue());
			}
	}

	private Double expectedResultFor(Granularity granularity, Metric metric) {
		String expression = expectedResults.get("" + granularity).get(metric.getName());
		String[] values = expression.split("/", 2);
		if (values.length > 1)
			return Double.parseDouble(values[0]) / Double.parseDouble(values[1]);
		return Double.parseDouble(expression);
	}
}