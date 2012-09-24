package org.kalibro.service.xml;

import static org.kalibro.core.model.MetricFixtures.analizoMetric;

import org.junit.Test;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.dto.AbstractDtoTest;
import org.kalibro.service.xml.MetricXmlTest.MyMetric;

public class MetricXmlTest extends AbstractDtoTest<MyMetric> {

	@Override
	protected MyMetric loadFixture() {
		NativeMetric nativeMetric = analizoMetric("cbo");
		return new MyMetric(nativeMetric.getName(), nativeMetric.getScope(), nativeMetric.getDescription());
	}

	@Test
	@Override
	public void shouldConvert() {
		assertDeepEquals(entity, new MyMetricXml(entity).convert());
	}

	class MyMetricXml extends MetricXml<MyMetric> {

		protected MyMetricXml() {
			super();
		}

		private MyMetricXml(MyMetric metric) {
			super(metric);
		}

		@Override
		public MyMetric convert() {
			return new MyMetric(name, scope, description);
		}
	}

	class MyMetric extends Metric {

		protected MyMetric(String name, Granularity scope, String description) {
			super(false, name, scope);
			setDescription(description);
		}
	}
}