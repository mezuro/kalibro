package org.kalibro.core.persistence.record;

import org.junit.Test;
import org.kalibro.ReadingGroup;

public class ReadingGroupRecordTest extends RecordTest<ReadingGroup, ReadingGroupRecord> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("scholar", ReadingGroup.class);
	}

	@Test
	public void verifyColumns() {
		assertId();
		assertColumn("name", String.class, false, true);
		assertColumn("description", String.class, true, false);
		assertOneToMany("readings", "group");
	}
}