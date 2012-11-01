package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.powermock.reflect.Whitebox;

public class ProcessingRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		assertManyToOne("repository", RepositoryRecord.class).isRequired();
		shouldHaveId();
		assertColumn("date", Long.class).isRequired();
		assertColumn("state", String.class).isRequired();
		shouldHaveError("error");
		assertOneToMany("processTimes").cascades().isMappedBy("processing");
		assertOneToOne("resultsRoot", ModuleResultRecord.class).doesNotCascade();
	}

	@Test
	public void shouldConvertNullErrorForNormalProcessing() {
		Processing normalProcessing = new Processing(new Repository());
		assertNull(new ProcessingRecord(normalProcessing).error());
	}

	@Test
	public void shouldRetrieveResultsRootId() {
		Whitebox.setInternalState(dto, "resultsRoot", new ModuleResultRecord(42L));
		assertEquals(42L, ((ProcessingRecord) dto).resultsRootId().longValue());
	}

	@Test
	public void checkNullResultsRoot() {
		assertNull(((ProcessingRecord) dto).resultsRootId());
	}
}