package org.kalibro.core.persistence.record;

import org.kalibro.NativeMetric;

public class NativeMetricRecordTest extends RecordTest<NativeMetric> {

	@Override
	protected NativeMetric loadFixture() {
		return loadFixture("cbo", NativeMetric.class);
	}
}