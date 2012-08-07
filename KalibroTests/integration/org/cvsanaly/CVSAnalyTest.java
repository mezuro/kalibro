package org.cvsanaly;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;

public class CVSAnalyTest extends KalibroTestCase {

	private CVSAnalyMetricCollector cvsanaly;

	@Before
	public void setUp() {
		//TODO Quiet logging
		cvsanaly = new CVSAnalyMetricCollector();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File(SAMPLES_DIRECTORY, "cvsanaly");
		Set<NativeMetric> metrics = cvsanaly.getBaseTool().getSupportedMetrics();
		assertDeepEquals(CVSAnalyStub.results(), cvsanaly.collectMetrics(codeDirectory, metrics));
	}
	
	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectSomeMetrics() throws Exception {
		File codeDirectory = new File(SAMPLES_DIRECTORY, "cvsanaly");
		Set<NativeMetric> metrics = new HashSet<NativeMetric>();
		metrics.add(CVSAnalyMetric.NUMBER_OF_LINES_OF_CODE.getNativeMetric());
		metrics.add(CVSAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY.getNativeMetric());

		assertDeepEquals(CVSAnalyStub.limitedResults(), cvsanaly.collectMetrics(codeDirectory, metrics));
	}
}