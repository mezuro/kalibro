package org.kalibro.service.entities;

import static org.junit.Assert.*;
import static org.kalibro.core.model.NativeMetricFixtures.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.service.entities.MetricXmlTest.MyMetric;

public class MetricXmlTest extends DtoTestCase<MyMetric, MetricXml<MyMetric>> {

	@Override
	public void defaultConstructorShouldDoNothing() {
		MyMetric metric = newDtoUsingDefaultConstructor().convert();
		assertNull(metric.getName());
		assertNull(metric.getScope());
		assertNull(metric.getDescription());
	}

	@Override
	protected MetricXml<MyMetric> newDtoUsingDefaultConstructor() {
		return new MyMetricXml();
	}

	@Override
	protected Collection<MyMetric> entitiesForTestingConversion() {
		List<MyMetric> metrics = new ArrayList<MyMetric>();
		for (NativeMetric nativeMetric : nativeMetrics())
			metrics.add(new MyMetric(nativeMetric.getName(), nativeMetric.getScope(), nativeMetric.getDescription()));
		return metrics;
	}

	@Override
	protected MetricXml<MyMetric> createDto(MyMetric metric) {
		return new MyMetricXml(metric);
	}

	class MyMetricXml extends MetricXml<MyMetric> {

		private MyMetricXml() {
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

		private MyMetric(String name, Granularity scope, String description) {
			super(name, scope);
			setDescription(description);
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}
}