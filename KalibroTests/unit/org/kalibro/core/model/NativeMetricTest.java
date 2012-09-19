package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.analizoStub;
import static org.kalibro.core.model.enums.Granularity.*;
import static org.kalibro.core.model.enums.Language.*;

import org.junit.Test;
import org.kalibro.TestCase;

public class NativeMetricTest extends TestCase {

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