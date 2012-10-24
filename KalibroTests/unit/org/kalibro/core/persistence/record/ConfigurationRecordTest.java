package org.kalibro.core.persistence.record;

public class ConfigurationRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("name", String.class).isRequired().isUnique();
		assertColumn("description", String.class).isNullable();
		assertOneToMany("metricConfigurations").isMappedBy("configuration");
	}
}