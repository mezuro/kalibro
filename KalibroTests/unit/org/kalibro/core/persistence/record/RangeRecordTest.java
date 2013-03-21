package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Range;

public class RangeRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("configuration", Long.class).isRequired();
		assertColumn("beginning", Long.class).isRequired();
		assertColumn("end", Long.class).isRequired();
		assertColumn("reading", Long.class).isNullable();
		assertColumn("comments", String.class).isNullable();
	}

	@Test
	public void shouldRetrieveReadingId() {
		Range range = (Range) entity;
		RangeRecord record = (RangeRecord) dto;
		assertEquals(range.getReading().getId(), record.readingId());
	}

	@Test
	public void checkNullReading() {
		assertNull(new RangeRecord(new Range()).readingId());
	}
}