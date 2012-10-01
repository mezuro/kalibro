package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.METHOD;
import static org.kalibro.Language.*;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeMetricTest extends UnitTest {

	@Test
	public void checkConstruction() {
		NativeMetric metric = new NativeMetric("NativeMetricTest name", METHOD, JAVA, CPP);
		assertFalse(metric.isCompound());
		assertEquals("NativeMetricTest name", metric.getName());
		assertEquals(METHOD, metric.getScope());
		assertDeepEquals(asSet(JAVA, CPP), metric.getLanguages());
	}
}