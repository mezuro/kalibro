package org.kalibro.core.persistence.record;

import static org.kalibro.MetricFixtures.sc;

import org.kalibro.MetricConfiguration;

public class MetricConfigurationRecordTest extends RecordTest<MetricConfiguration> {

	@Override
	protected MetricConfiguration loadFixture() {
		return new MetricConfiguration(sc());
	}
}