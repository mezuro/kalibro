package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProcessingObserver;
import org.kalibro.core.persistence.record.ProcessingObserverRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessingObserverDatabaseDao.class)
public class ProcessingObserverDatabaseDaoTest extends
	DatabaseDaoTestCase<ProcessingObserver, ProcessingObserverRecord, ProcessingObserverDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Test
	public void shouldGetObserversOfRepository() {
		assertDeepEquals(set(entity), dao.observersOf(REPOSITORY_ID));
		verify(dao).createRecordQuery("processingObserver.repository = :repository");
		verify(query).setParameter("repository", REPOSITORY_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, REPOSITORY_ID));

		verifyNew(ProcessingObserverRecord.class).withArguments(entity, REPOSITORY_ID);
		verify(dao).save(record);
	}

}