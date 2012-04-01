package org.kalibro.core.model;

import static org.analizo.AnalizoStub.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;
import static org.kalibro.core.model.enums.Language.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class NativeMetricTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInitialization() {
		NativeMetric metric = new NativeMetric("", CLASS, JAVA, CPP);
		assertEquals("", metric.getName());
		assertEquals(CLASS, metric.getScope());
		assertDeepEquals(metric.getLanguages(), JAVA, CPP);
		assertEquals("", metric.getDescription());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void nativeMetricsShouldNotBeCompound() {
		for (NativeMetric metric : nativeMetrics())
			assertFalse(metric.isCompound());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testOrigin() {
		NativeMetric metric = new NativeMetric("", PACKAGE, C);
		assertNull(metric.getOrigin());

		String origin = "Analizo";
		metric.setOrigin(origin);
		assertSame(origin, metric.getOrigin());
	}
}