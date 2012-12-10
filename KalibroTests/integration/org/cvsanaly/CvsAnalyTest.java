package org.cvsanaly;

import java.util.Set;

import org.kalibro.NativeMetric;
import org.kalibro.tests.CollectorIntegrationTest;

public class CvsAnalyTest extends CollectorIntegrationTest<CvsAnalyMetricCollector> {

	@Override
	protected String expectedName() {
		return "CVSAnalY";
	}

	@Override
	protected Set<NativeMetric> expectedSupportedMetrics() {
		return CvsAnalyMetric.supportedMetrics();
	}
}