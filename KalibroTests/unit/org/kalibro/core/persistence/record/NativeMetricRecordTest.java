package org.kalibro.core.persistence.record;

import org.kalibro.MetricFixtures;
import org.kalibro.NativeMetric;

public class NativeMetricRecordTest extends RecordTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return MetricFixtures.analizoMetric("loc");
	}
}