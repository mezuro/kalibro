package org.analizo;

import static org.analizo.AnalizoStub.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileProcessStreamLogger.class)
public class AnalizoTest extends KalibroTestCase {

	@BeforeClass
	public static void checkAnalizoVersion() {
		try {
			String message = "The Analizo version installed is not the expected for this test.";
			assertEquals(message, "1.16.0", getAnalizoVersion());
		} catch (Exception exception) {
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
	public void checkSupportedMetrics() {
		assertDeepEquals(nativeMetrics(), analizo.getSupportedMetrics());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectMetrics() throws IOException {
		File codeDirectory = new File(SAMPLES_DIRECTORY, "analizo");
		assertDeepEquals(collectMetrics(), analizo.collectMetrics(codeDirectory, nativeMetrics()));
	}
}