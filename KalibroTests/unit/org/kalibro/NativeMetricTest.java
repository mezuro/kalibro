package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.BaseToolFixtures.analizoStub;
import static org.kalibro.Granularity.*;
import static org.kalibro.Language.*;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeMetricTest extends UnitTest {

	@Test
	public void checkInitialization() {
		NativeMetric metric = new NativeMetric("", CLASS, JAVA, CPP);
		assertEquals("", metric.getName());
		assertEquals(CLASS, metric.getScope());
		assertDeepEquals(asList(JAVA, CPP), metric.getLanguages());
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
	}
}