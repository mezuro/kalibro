package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.kalibro.Processing;
import org.kalibro.Repository;

public class ProcessingRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("repository", RepositoryRecord.class).isRequired();
		shouldHaveId();
		assertColumn("date", Long.class).isRequired();
		assertColumn("state", String.class).isRequired();
		assertOneToOne("error", ThrowableRecord.class).isOptional();
		assertOneToMany("processTimes").isMappedBy("processing");
	}

	@Test
	public void shouldConvertNullErrorForNormalProcessing() {
		Processing normalProcessing = new Processing(new Repository());
		assertNull(new ProcessingRecord(normalProcessing).error());
	}
}