package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.kalibro.Range;

public class RangeSnapshotRecordTest extends RecordTest {

	@Override
	public String entityName() {
		return "RangeSnapshot";
	}

	@Override
	protected void verifyColumns() {
		assertManyToOne("configurationSnapshot", MetricConfigurationSnapshotRecord.class).isRequired();
		shouldHaveId();
		assertColumn("beginning", Long.class).isRequired();
		assertColumn("end", Long.class).isRequired();
		assertColumn("comments", String.class).isNullable();
		assertColumn("label", String.class).isNullable();
		assertColumn("grade", Long.class).isNullable();
		assertColumn("color", Integer.class).isNullable();
	}

	@Test
	public void shouldConstructWithoutReading() {
		assertNull(new RangeSnapshotRecord(new Range()).reading());
	}
}