package org.kalibro.core.persistence.database.entities;

import static org.kalibro.core.model.BaseToolFixtures.analizoStub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.DtoTestCase;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.persistence.database.entities.MetricRecordTest.MyMetric;

public class MetricRecordTest extends DtoTestCase<MyMetric, MetricRecord<MyMetric>> {

	@Override
	protected MetricRecord<MyMetric> newDtoUsingDefaultConstructor() {
		return new MyMetricRecord();
	}

	@Override
	protected Collection<MyMetric> entitiesForTestingConversion() {
		List<MyMetric> metrics = new ArrayList<MyMetric>();
		for (NativeMetric nativeMetric : analizoStub().getSupportedMetrics())
			metrics.add(new MyMetric(nativeMetric.getName(), nativeMetric.getScope(), nativeMetric.getDescription()));
		return metrics;
	}

	@Override
	protected MetricRecord<MyMetric> createDto(MyMetric metric) {
		return new MyMetricRecord(metric);
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