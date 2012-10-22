package org.checkstyle;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.NativeMetric;
import org.kalibro.tests.UnitTest;

public class PreModuleResultTest extends UnitTest {

	private static final CheckstyleMetric METRIC = CheckstyleMetric.metricFor("maxLen.file");
	private static final Module MODULE = new Module(Granularity.CLASS, "PreModuleResultTestModule");

	private PreModuleResult result;

	@Before
	public void setUp() {
		Set<NativeMetric> wantedMetrics = set((NativeMetric) METRIC);
		result = new PreModuleResult(MODULE, wantedMetrics);
	}

	@Test
	public void shouldRetrieveModule() {
		assertSame(MODULE, result.getModuleResult().getModule());
	}

	@Test
	public void shouldRetrieveMetricResultEvenWithoutAdding() {
		assertDoubleEquals(0.0, result.getModuleResult().getResultFor(METRIC).getValue());
	}

	@Test
	public void shouldRetrieveMetricResultWithAddedValues() {
		result.addMetricResult(METRIC.getMessageKey(), 28.0);
		result.addMetricResult(METRIC.getMessageKey(), 14.0);
		assertDoubleEquals(42.0, result.getModuleResult().getResultFor(METRIC).getValue());
	}

	@Test
	public void shouldNotRetrieveMetricResultsForUnwantedMetrics() {
		CheckstyleMetric unwantedMetric = CheckstyleMetric.metricFor("too.many.methods");
		result.addMetricResult(unwantedMetric.getMessageKey(), 42.0);
		assertFalse(result.getModuleResult().hasResultFor(unwantedMetric));
	}
}