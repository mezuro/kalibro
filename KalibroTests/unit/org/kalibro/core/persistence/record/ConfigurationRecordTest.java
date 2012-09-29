package org.kalibro.core.persistence.record;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.ConfigurationFixtures;

public class ConfigurationRecordTest extends RecordTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return ConfigurationFixtures.kalibroConfiguration();
	}

	@Test
	public void verifyColumns() {
		assertId();
		assertColumn("name", String.class, false, true);
		assertColumn("description", String.class, true, false);
		assertOneToMany("metricConfigurations", "configuration");
	}
}