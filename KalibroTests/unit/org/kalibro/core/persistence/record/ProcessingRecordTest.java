package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;

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
		ModuleResult resultsRoot = mock(ModuleResult.class);
		when(resultsRoot.getId()).thenReturn(42L);
		Processing processing = (Processing) entity;
		processing.setResultsRoot(resultsRoot);
		assertEquals(42L, new ProcessingRecord(processing).resultsRootId().longValue());
	}

	@Test
	public void checkNullResultsRoot() {
		assertNull(((ProcessingRecord) dto).resultsRootId());
	}
}