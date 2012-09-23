package org.kalibro.core.persistence.record;

import static org.kalibro.core.model.ConfigurationFixtures.kalibroConfiguration;

import org.kalibro.Configuration;

public class ConfigurationRecordTest extends RecordTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return kalibroConfiguration();
	}
}