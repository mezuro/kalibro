package org.analizo;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
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

	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws IOException {
		analizo = new AnalizoMetricCollector();
	}

	@Test
	public void shouldGetSupportedMetrics() {
		assertDeepEquals(loadYaml("supported-metrics", Set.class), analizo.supportedMetrics());
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		Producer<NativeModuleResult> producer = new Producer<NativeModuleResult>();

		File codeDirectory = new File(samplesDirectory(), "analizo");
		Set<NativeMetric> wantedMetrics = analizo.supportedMetrics();
		Writer<NativeModuleResult> resultWriter = producer.createWriter();
		analizo.collectMetrics(codeDirectory, wantedMetrics, resultWriter);

		Iterator<NativeModuleResult> iterator = producer.iterator();
		assertDeepEquals(loadResult(SOFTWARE), iterator.next());
		assertDeepEquals(loadResult(CLASS), iterator.next());
		assertFalse(iterator.hasNext());
	}

	private NativeModuleResult loadResult(Granularity granularity) {
		return loadYaml("result-" + granularity + "-HelloWorld", NativeModuleResult.class);
	}
}