package org.analizo;

import java.util.Set;

import org.kalibro.NativeMetric;
import org.kalibro.tests.CollectorIntegrationTest;

public class AnalizoTest extends CollectorIntegrationTest<AnalizoMetricCollector> {

	@Override
	protected String expectedName() {
		return "Analizo";
	}

	@Override
	protected Set<? extends NativeMetric> expectedSupportedMetrics() {
		return loadYaml("supported-metrics", Set.class);
	}
}