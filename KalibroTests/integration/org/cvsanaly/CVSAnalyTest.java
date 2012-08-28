package org.cvsanaly;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.NativeMetric;

public class CVSAnalyTest extends TestCase {

	private CVSAnalyMetricCollector cvsanaly;
	private PrintStream originalSysout;

	@Before
	public void setUp() {
		originalSysout = System.out;
		System.setOut(new PrintStream(new OutputStream() {

			@Override
			public void write(int arg0) throws IOException { /* Do nothing. This is to silence the console. */}
		}));
		cvsanaly = new CVSAnalyMetricCollector();
	}

	@After
	public void tearDown() {
		System.setOut(originalSysout);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File(samplesDirectory(), "cvsanaly");
		Set<NativeMetric> metrics = cvsanaly.getBaseTool().getSupportedMetrics();
		assertDeepEquals(CVSAnalyStub.results(), cvsanaly.collectMetrics(codeDirectory, metrics));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectSomeMetrics() throws Exception {
		File codeDirectory = new File(samplesDirectory(), "cvsanaly");
		Set<NativeMetric> metrics = new HashSet<NativeMetric>();
		metrics.add(CVSAnalyMetric.NUMBER_OF_LINES_OF_CODE.getNativeMetric());
		metrics.add(CVSAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY.getNativeMetric());

		assertDeepEquals(CVSAnalyStub.limitedResults(), cvsanaly.collectMetrics(codeDirectory, metrics));
	}
}