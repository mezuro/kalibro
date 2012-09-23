package org.kalibro.core.model;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Test;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.tests.UnitTest;

public class MetricTest extends UnitTest {

	@Test
	public void defaultDescriptionShouldBeEmpty() {
		Metric metric = new MyMetric();
		assertEquals("", metric.getDescription());
	}

	@Test
	public void toStringShouldBeMetricName() {
		assertEquals("acc", "" + new MyMetric("acc"));
		assertEquals("loc", "" + new MyMetric("loc"));
		assertEquals("nom", "" + new MyMetric("nom"));
	}

	@Test
	public void shouldSortByCompoundThenScopeThenName() {
		assertSorted(
			new MyMetric(false, "G", CLASS), new MyMetric(false, "H", CLASS),
			new MyMetric(false, "E", METHOD), new MyMetric(false, "F", METHOD),
			new MyMetric(true, "C", SOFTWARE), new MyMetric(true, "D", SOFTWARE),
			new MyMetric(true, "A", PACKAGE), new MyMetric(true, "B", PACKAGE));
	}

	private class MyMetric extends Metric {

		private MyMetric() {
			this("");
		}

		private MyMetric(String name) {
			this(false, name, CLASS);
		}

		public MyMetric(boolean compound, String name, Granularity scope) {
			super(compound, name, scope);
		}
	}
}