package org.checkstyle;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;

public class PreModuleResultTest extends TestCase {

	private static final CheckstyleMetric METRIC = CheckstyleMetric.FAN_OUT;
	private static final NativeMetric NATIVE_METRIC = METRIC.getNativeMetric();
	private static final Module MODULE = new Module(Granularity.CLASS, "PreModuleResultTestModule");

	private PreModuleResult result;

	@Before
	public void setUp() {
		HashSet<NativeMetric> wantedMetrics = new HashSet<NativeMetric>(Arrays.asList(NATIVE_METRIC));
		result = new PreModuleResult(MODULE, wantedMetrics);
	}

	@Test
	public void shouldRetrieveModule() {
		assertSame(MODULE, result.getModuleResult().getModule());
	}

	@Test
	public void shouldRetrieveMetricResultEvenWithoutAdding() {
		assertDoubleEquals(0.0, result.getModuleResult().getResultFor(NATIVE_METRIC).getValue());
	}

	@Test
	public void shouldRetrieveMetricResultWithAddedValues() {
		result.addMetricResult(METRIC.getMessageKey(), 28.0);
		result.addMetricResult(METRIC.getMessageKey(), 14.0);
		assertDoubleEquals(42.0, result.getModuleResult().getResultFor(NATIVE_METRIC).getValue());
	}

	@Test
	public void shouldNotRetrieveMetricResultsForUnwantedMetrics() {
		CheckstyleMetric unwantedMetric = CheckstyleMetric.FILE_LENGTH;
		result.addMetricResult(unwantedMetric.getMessageKey(), 42.0);
		assertFalse(result.getModuleResult().hasResultFor(unwantedMetric.getNativeMetric()));
	}
}