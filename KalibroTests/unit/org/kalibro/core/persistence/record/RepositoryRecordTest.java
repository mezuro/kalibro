package org.kalibro.core.persistence.record;

public class RepositoryRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
	}
}