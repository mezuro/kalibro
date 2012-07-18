package org.cvsanaly;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;

public class CVSAnalyTest extends KalibroTestCase {

	private CVSAnalyMetricCollector cvsanaly;

	@Before
	public void setUp() {
		cvsanaly = new CVSAnalyMetricCollector();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File(SAMPLES_DIRECTORY, "cvsanaly");
		Set<NativeMetric> metrics = cvsanaly.getBaseTool().getSupportedMetrics();
		assertDeepEquals(CVSAnalyStub.results(), cvsanaly.collectMetrics(codeDirectory, metrics));
	}
}