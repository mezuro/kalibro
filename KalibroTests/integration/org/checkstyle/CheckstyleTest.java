package org.checkstyle;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.NativeMetric;

public class CheckstyleTest extends TestCase {

	private CheckstyleMetricCollector checkstyle;

	@Before
	public void setUp() {
		checkstyle = new CheckstyleMetricCollector();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File samplesDirectory = new File(samplesDirectory(), "checkstyle");
		File codeDirectory = new File(samplesDirectory, "Fibonacci");
		Set<NativeMetric> metrics = checkstyle.getBaseTool().getSupportedMetrics();
		assertDeepEquals(CheckstyleStub.results(), checkstyle.collectMetrics(codeDirectory, metrics));
	}
}