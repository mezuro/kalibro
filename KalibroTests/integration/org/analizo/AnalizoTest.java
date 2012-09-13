package org.analizo;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.analizo;
import static org.kalibro.core.model.NativeModuleResultFixtures.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.IntegrationTest;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.kalibro.core.model.NativeMetric;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileProcessStreamLogger.class)
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
		MemberModifier.suppress(FileProcessStreamLogger.class.getMethods());
		InputStream output = new CommandTask("analizo --version").executeAndGetOuput();
		return IOUtils.toString(output).replace("analizo version", "").trim();
	}

	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws IOException {
		MemberModifier.suppress(FileProcessStreamLogger.class.getMethods());
		analizo = new AnalizoMetricCollector();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void checkBaseTool() {
		assertDeepEquals(analizo(), analizo.getBaseTool());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectMetrics() throws IOException {
		File codeDirectory = new File(samplesDirectory(), "analizo");
		Set<NativeMetric> metrics = analizo().getSupportedMetrics();
		assertDeepSet(analizo.collectMetrics(codeDirectory, metrics),
			helloWorldApplicationResult(), helloWorldClassResult());
	}
}