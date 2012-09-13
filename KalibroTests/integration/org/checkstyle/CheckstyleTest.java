package org.checkstyle;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.IntegrationTest;
import org.kalibro.core.model.NativeMetric;

public class CheckstyleTest extends IntegrationTest {

	private CheckstyleMetricCollector checkstyle;

	@Before
	public void setUp() {
		checkstyle = new CheckstyleMetricCollector();
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		File samplesDirectory = new File(samplesDirectory(), "checkstyle");
		File codeDirectory = new File(samplesDirectory, "Fibonacci");
		Set<NativeMetric> metrics = checkstyle.getBaseTool().getSupportedMetrics();
		assertDeepEquals(CheckstyleStub.results(), checkstyle.collectMetrics(codeDirectory, metrics));
	}
}