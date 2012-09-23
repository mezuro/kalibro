package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.MetricFixtures.sc;

import org.kalibro.core.model.CompoundMetric;

public class CompoundMetricRecordTest extends RecordTest<CompoundMetric> {

	@Override
	protected CompoundMetric loadFixture() {
		return sc();
	}
}