package org.cvsanaly;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.IntegrationTest;

public class CvsAnalyTest extends IntegrationTest {

	private CvsAnalyMetricCollector cvsanaly;

	private Writer<NativeModuleResult> resultWriter;

	@Before
	public void setUp() {
		cvsanaly = new CvsAnalyMetricCollector();
		resultWriter = mock(Writer.class);
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File(samplesDirectory(), "cvsanaly");
		Set<NativeMetric> metrics = cvsanaly.supportedMetrics();
		cvsanaly.collectMetrics(codeDirectory, metrics, resultWriter);
		verifyResults(CvsAnalyStub.results());
	}

	@Test
	public void shouldCollectSomeMetrics() throws Exception {
		File codeDirectory = new File(samplesDirectory(), "cvsanaly");
		Set<NativeMetric> metrics = new HashSet<NativeMetric>();
		metrics.add(CvsAnalyMetric.NUMBER_OF_LINES_OF_CODE.getNativeMetric());
		metrics.add(CvsAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY.getNativeMetric());

		cvsanaly.collectMetrics(codeDirectory, metrics, resultWriter);
		verifyResults(CvsAnalyStub.limitedResults());
	}

	private void verifyResults(Set<NativeModuleResult> expected) {
		for (NativeModuleResult moduleResult : expected)
			verify(resultWriter).write(deepEq(moduleResult));
		verify(resultWriter).close();
	}
}