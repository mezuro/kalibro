package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;
import static org.kalibro.Language.*;
import static org.kalibro.core.model.BaseToolFixtures.analizoStub;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeMetricTest extends UnitTest {

	@Test
	public void checkInitialization() {
		NativeMetric metric = new NativeMetric("", CLASS, JAVA, CPP);
		assertEquals("", metric.getName());
		assertEquals(CLASS, metric.getScope());
		assertDeepList(metric.getLanguages(), JAVA, CPP);
		assertEquals("", metric.getDescription());
	}

	@Test
	public void nativeMetricsShouldNotBeCompound() {
		for (NativeMetric metric : analizoStub().getSupportedMetrics())
			assertFalse(metric.isCompound());
	}

	@Test
	public void testOrigin() {
		NativeMetric metric = new NativeMetric("", PACKAGE, C);
		assertNull(metric.getOrigin());

		String origin = "Analizo";
		metric.setOrigin(origin);
		assertSame(origin, metric.getOrigin());
	}
}