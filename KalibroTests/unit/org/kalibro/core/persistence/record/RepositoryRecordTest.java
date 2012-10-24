package org.kalibro.core.persistence.record;

public class RepositoryRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("project", ProjectRecord.class).isRequired();
		shouldHaveId();
		assertColumn("name", String.class).isRequired();
		assertColumn("type", String.class).isRequired();
		assertColumn("address", String.class).isRequired();
		assertColumn("description", String.class).isNullable();
		assertColumn("license", String.class).isNullable();
		assertColumn("processPeriod", Integer.class).isNullable();
		assertManyToOne("configuration", ConfigurationRecord.class).isRequired();
	}
}