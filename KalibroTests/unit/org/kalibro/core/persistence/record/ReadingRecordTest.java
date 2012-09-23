package org.kalibro.core.persistence.record;

import org.junit.Test;
import org.kalibro.Reading;

public class ReadingRecordTest extends RecordTest<Reading, ReadingRecord> {

	@Override
	protected Reading loadFixture() {
		return loadFixture("excellent", Reading.class);
	}

	@Test
	public void verifyColumns() {
		assertId();
		assertColumn("label", String.class, false, false);
		assertColumn("grade", Long.class, false, false);
		assertColumn("color", Integer.class, false, false);
		assertManyToOne("group", ReadingGroupRecord.class, false);
	}
}