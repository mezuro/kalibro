package org.analizo;

import static org.junit.Assert.*;
import static org.kalibro.BaseToolFixtures.analizo;
import static org.kalibro.NativeModuleResultFixtures.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.core.command.CommandTask;
import org.kalibro.tests.IntegrationTest;

public class AnalizoTest extends IntegrationTest {

	@BeforeClass
	public static void checkAnalizoVersion() {
		try {
			String message = "The Analizo version installed is not the expected for this test.";
			assertEquals(message, "1.16.0", getAnalizoVersion());
		} catch (IOException exception) {
			fail("Analizo is not installed but is required for this test.");
		}
	}

	private static String getAnalizoVersion() throws IOException {
		InputStream output = new CommandTask("analizo --version").executeAndGetOuput();
		return IOUtils.toString(output).replace("analizo version", "").trim();
	}

	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws IOException {
		analizo = new AnalizoMetricCollector();
	}

	@Test
	public void checkBaseTool() {
		assertDeepEquals(analizo(), analizo.getBaseTool());
	}

	@Test
	public void shouldCollectMetrics() throws IOException {
		File codeDirectory = new File(samplesDirectory(), "analizo");
		Set<NativeMetric> metrics = analizo().getSupportedMetrics();
		assertDeepEquals(asList(helloWorldApplicationResult(), helloWorldClassResult()),
			analizo.collectMetrics(codeDirectory, metrics));
	}
}