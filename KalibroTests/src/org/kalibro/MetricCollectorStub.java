package org.kalibro;

import static org.kalibro.tests.SpecialAssertions.asSet;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.util.Set;

class MetricCollectorStub implements MetricCollector {

	static final String NAME = "Metric collector stub";
	static final String DESCRIPTION = "A MetricCollector for testing";
	static final String CLASS_NAME = MetricCollectorStub.class.getName();
	static final NativeMetric SUPPORTED_METRIC = mock(NativeMetric.class);
	static final NativeModuleResult RESULT = mock(NativeModuleResult.class);

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public String description() {
		return DESCRIPTION;
	}

	@Override
	public Set<NativeMetric> supportedMetrics() {
		return asSet(SUPPORTED_METRIC);
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> wantedMetrics)
		throws Exception {
		return asSet(RESULT);
	}
}