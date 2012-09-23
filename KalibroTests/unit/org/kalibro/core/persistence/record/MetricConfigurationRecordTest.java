package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.MetricFixtures.sc;

import org.kalibro.core.model.MetricConfiguration;

public class MetricConfigurationRecordTest extends RecordTest<MetricConfiguration> {

	@Override
	protected MetricConfiguration loadFixture() {
		return new MetricConfiguration(sc());
	}
}