package org.checkstyle;

import java.util.Set;

import org.kalibro.NativeMetric;
import org.kalibro.tests.CollectorIntegrationTest;

public class CheckstyleTest extends CollectorIntegrationTest<CheckstyleMetricCollector> {

	@Override
	protected String expectedName() {
		return "Checkstyle";
	}

	@Override
	protected Set<NativeMetric> expectedSupportedMetrics() {
		return CheckstyleMetric.supportedMetrics();
	}
}