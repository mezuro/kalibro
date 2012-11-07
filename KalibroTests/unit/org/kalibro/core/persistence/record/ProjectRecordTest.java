package org.kalibro.core.persistence.record;

public class ProjectRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("name", String.class).isRequired().isUnique();
		assertColumn("description", String.class).isNullable();
		assertOneToMany("repositories").doesNotCascade().isMappedBy("project");
	}
}