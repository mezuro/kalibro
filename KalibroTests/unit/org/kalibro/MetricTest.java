package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class MetricTest extends UnitTest {

	@Test
	public void shouldSortByCompoundThenScopeThenName() {
		assertSorted(
			metric(false, "G", CLASS), metric(false, "H", CLASS),
			metric(false, "E", METHOD), metric(false, "F", METHOD),
			metric(true, "C", SOFTWARE), metric(true, "D", SOFTWARE),
			metric(true, "A", PACKAGE), metric(true, "B", PACKAGE));
	}

	@Test
	public void shouldDetermineEqualityByName() {
		assertEquals(metric(true, "X", SOFTWARE), metric(false, "X", METHOD));
	}

	@Test
	public void checkConstruction() {
		Metric metric = metric(false, "MetricTest name", PACKAGE);
		assertFalse(metric.isCompound());
		assertEquals("MetricTest name", metric.getName());
		assertEquals(PACKAGE, metric.getScope());
		assertEquals("", metric.getDescription());
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals("X", "" + metric(false, "X", SOFTWARE));
		assertEquals("Y", "" + metric(true, "Y", PACKAGE));
		assertEquals("Z", "" + metric(true, "Z", CLASS));
	}

	private Metric metric(boolean compound, String name, Granularity scope) {
		return new Metric(compound, name, scope) {/* just for test */};
	}
}