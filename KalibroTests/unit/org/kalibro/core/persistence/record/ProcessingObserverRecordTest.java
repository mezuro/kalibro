package org.kalibro.core.persistence.record;

public class ProcessingObserverRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("name", String.class).isRequired();
		assertColumn("email", String.class).isRequired();
		assertColumn("repository", Long.class).isRequired();
	}

}
