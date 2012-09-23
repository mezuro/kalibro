package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.MetricResultFixtures.newMetricResult;

import org.kalibro.core.model.MetricResult;

public class MetricResultRecordTest extends RecordTest<MetricResult> {

	@Override
	protected MetricResult loadFixture() {
		Double[] specialvalues = new Double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
		return newMetricResult("loc", 42.0, specialvalues);
	}
}