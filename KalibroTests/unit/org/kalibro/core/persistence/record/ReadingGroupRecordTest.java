package org.kalibro.core.persistence.record;

public class ReadingGroupRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("name", String.class).isRequired().isUnique();
		assertColumn("description", String.class).isNullable();
	}
}