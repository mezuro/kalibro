package org.kalibro;

import static org.kalibro.Granularity.METHOD;
import static org.kalibro.tests.SpecialAssertions.set;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.util.Set;

import org.kalibro.core.concurrent.Writer;

public class MetricCollectorStub implements MetricCollector {

	public static final String NAME = "Metric collector stub";
	public static final String DESCRIPTION = "A MetricCollector for testing";
	public static final String CLASS_NAME = MetricCollectorStub.class.getName();
	public static final NativeMetric SUPPORTED_METRIC = new NativeMetric("MetriCollectorStub metric", METHOD);
	public static final NativeModuleResult RESULT = mock(NativeModuleResult.class);

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
		return set(SUPPORTED_METRIC);
	}

	@Override
	public void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		resultWriter.write(RESULT);
		resultWriter.close();
	}
}