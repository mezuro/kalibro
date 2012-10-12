package org.cvsanaly;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.tests.IntegrationTest;

public class CVSAnalyTest extends IntegrationTest {

	private CvsAnalyMetricCollector cvsanaly;

	@Before
	public void setUp() {
		cvsanaly = new CvsAnalyMetricCollector();
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File(samplesDirectory(), "cvsanaly");
		Set<NativeMetric> metrics = cvsanaly.supportedMetrics();
		assertDeepEquals(CVSAnalyStub.results(), cvsanaly.collectMetrics(codeDirectory, metrics));
	}

	@Test
	public void shouldCollectSomeMetrics() throws Exception {
		File codeDirectory = new File(samplesDirectory(), "cvsanaly");
		Set<NativeMetric> metrics = new HashSet<NativeMetric>();
		metrics.add(CVSAnalyMetric.NUMBER_OF_LINES_OF_CODE.getNativeMetric());
		metrics.add(CVSAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY.getNativeMetric());

		assertDeepEquals(CVSAnalyStub.limitedResults(), cvsanaly.collectMetrics(codeDirectory, metrics));
	}
}