package org.kalibro.core.persistence.record;

import org.kalibro.core.model.MetricFixtures;
import org.kalibro.core.model.NativeMetric;

public class NativeMetricRecordTest extends RecordTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return MetricFixtures.analizoMetric("loc");
	}
}