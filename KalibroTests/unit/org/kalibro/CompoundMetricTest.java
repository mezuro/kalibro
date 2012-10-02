package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class CompoundMetricTest extends UnitTest {

	@Test
	public void checkConstruction() {
		CompoundMetric metric = new CompoundMetric();
		assertTrue(metric.isCompound());
		assertEquals("New metric", metric.getName());
		assertEquals(Granularity.CLASS, metric.getScope());
		assertEquals("return 1;", metric.getScript());
	}
}