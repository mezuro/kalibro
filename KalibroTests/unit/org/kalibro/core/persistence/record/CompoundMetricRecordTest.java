package org.kalibro.core.persistence.record;

import static org.kalibro.MetricFixtures.sc;

import org.kalibro.CompoundMetric;

public class CompoundMetricRecordTest extends RecordTest<CompoundMetric> {

	@Override
	protected CompoundMetric loadFixture() {
		return sc();
	}
}