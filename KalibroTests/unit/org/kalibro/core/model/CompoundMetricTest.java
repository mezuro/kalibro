package org.kalibro.core.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.Granularity;

public class CompoundMetricTest extends TestCase {

	private CompoundMetric metric;

	@Before
	public void setUp() {
		metric = new CompoundMetric();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInitialization() {
		assertEquals("New metric", metric.getName());
		assertEquals("", metric.getDescription());
		assertEquals(Granularity.CLASS, metric.getScope());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultScriptShouldReturn1() {
		assertEquals("return 1;", metric.getScript());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeCompound() {
		assertTrue(metric.isCompound());
	}
}