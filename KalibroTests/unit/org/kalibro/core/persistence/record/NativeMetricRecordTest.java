package org.kalibro.core.persistence.record;

import org.kalibro.NativeMetric;
import org.kalibro.core.model.MetricFixtures;

public class NativeMetricRecordTest extends RecordTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return MetricFixtures.analizoMetric("loc");
	}
}