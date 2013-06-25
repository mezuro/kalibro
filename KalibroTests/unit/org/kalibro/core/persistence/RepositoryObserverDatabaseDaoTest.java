package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.RepositoryObserver;
import org.kalibro.core.persistence.record.RepositoryObserverRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryObserverDatabaseDao.class)
public class RepositoryObserverDatabaseDaoTest extends
	DatabaseDaoTestCase<RepositoryObserver, RepositoryObserverRecord, RepositoryObserverDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Test
	public void shouldGetObserversOfRepository() {
		assertDeepEquals(set(entity), dao.observersOf(REPOSITORY_ID));
		verify(dao).createRecordQuery("repositoryObserver.repository = :repository");
		verify(query).setParameter("repository", REPOSITORY_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, REPOSITORY_ID));

		verifyNew(RepositoryObserverRecord.class).withArguments(entity, REPOSITORY_ID);
		verify(dao).save(record);
	}

}