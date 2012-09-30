package org.analizo;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;
import org.kalibro.tests.IntegrationTest;

public class AnalizoIntegrationTest extends IntegrationTest {

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

	private Map<NativeMetric, String> supportedMetrics;
	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws IOException {
		InputStream metricListOutput = getStream("analizo-metrics-list-complete");
		supportedMetrics = new AnalizoMetricListParser(metricListOutput).getSupportedMetrics();
		analizo = new AnalizoMetricCollector();
	}

	@Test
	public void shouldGetSupportedMetrics() {
		assertDeepEquals(supportedMetrics.keySet(), analizo.supportedMetrics());
	}

	@Test
	public void shouldCollectMetrics() throws IOException {
		File codeDirectory = new File(samplesDirectory(), "analizo");
		Set<NativeMetric> wantedMetrics = supportedMetrics.keySet();

		InputStream analizoOutput = getStream("analizo-metrics-HelloWorld-complete");
		Set<NativeModuleResult> results = new AnalizoResultParser(supportedMetrics, wantedMetrics).parse(analizoOutput);

		assertDeepEquals(results, analizo.collectMetrics(codeDirectory, wantedMetrics));
	}
}