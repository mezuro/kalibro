package org.kalibro.core.persistence.record;

public class ReadingRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("label", String.class).isRequired();
		assertColumn("grade", Long.class).isRequired();
		assertColumn("color", Integer.class).isRequired();
		assertManyToOne("readingGroup", ReadingGroupRecord.class).isRequired();
	}
}