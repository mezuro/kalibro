package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.kalibro.Range;
import org.powermock.reflect.Whitebox;

public class RangeRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("configuration", MetricConfigurationRecord.class).isRequired();
		shouldHaveId();
		assertColumn("beginning", Long.class).isRequired();
		assertColumn("end", Long.class).isRequired();
		assertColumn("comments", String.class).isNullable();
		assertManyToOne("reading", ReadingRecord.class).isOptional();
	}

	@Test
	public void shouldConstructWithoutReading() {
		RangeRecord record = new RangeRecord(new Range());
		assertNull(Whitebox.getInternalState(record, "reading"));
	}
}