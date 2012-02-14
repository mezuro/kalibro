package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.Granularity;

public class MetricTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultDescriptionShouldBeEmpty() {
		Metric metric = new MyMetric();
		assertEquals("", metric.getDescription());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void toStringShouldBeMetricName() {
		assertEquals("acc", "" + new MyMetric("acc"));
		assertEquals("loc", "" + new MyMetric("loc"));
		assertEquals("nom", "" + new MyMetric("nom"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByCompoundThenScopeThenName() {
		assertSorted(
			new MyMetric(false, "G", CLASS), new MyMetric(false, "H", CLASS),
			new MyMetric(false, "E", METHOD), new MyMetric(false, "F", METHOD),
			new MyMetric(true, "C", APPLICATION), new MyMetric(true, "D", APPLICATION),
			new MyMetric(true, "A", PACKAGE), new MyMetric(true, "B", PACKAGE));
	}

	private class MyMetric extends Metric {

		private boolean compound;

		private MyMetric() {
			this("");
		}

		private MyMetric(String name) {
			this(false, name, CLASS);
		}

		public MyMetric(boolean compound, String name, Granularity scope) {
			super(name, scope);
			this.compound = compound;
		}

		@Override
		public boolean isCompound() {
			return compound;
		}
	}
}