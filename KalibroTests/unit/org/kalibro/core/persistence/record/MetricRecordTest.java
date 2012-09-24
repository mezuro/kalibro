package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.MetricFixtures.analizoMetric;

import org.junit.Test;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.persistence.record.MetricRecordTest.MyMetric;
import org.kalibro.dto.AbstractDtoTest;

public class MetricRecordTest extends AbstractDtoTest<MyMetric> {

	@Override
	protected MyMetric loadFixture() {
		NativeMetric nativeMetric = analizoMetric("lcom4");
		return new MyMetric(nativeMetric.getName(), nativeMetric.getScope(), nativeMetric.getDescription());
	}

	@Test
	@Override
	public void shouldConvert() {
		assertDeepEquals(entity, new MyMetricRecord(entity).convert());
	}

	class MyMetricRecord extends MetricRecord<MyMetric> {

		protected MyMetricRecord() {
			super();
		}

		private MyMetricRecord(MyMetric metric) {
			super(metric);
		}

		@Override
		public MyMetric convert() {
			return new MyMetric(name, Granularity.valueOf(scope), description);
		}
	}

	class MyMetric extends Metric {

		protected MyMetric(String name, Granularity scope, String description) {
			super(false, name, scope);
			setDescription(description);
		}
	}
}