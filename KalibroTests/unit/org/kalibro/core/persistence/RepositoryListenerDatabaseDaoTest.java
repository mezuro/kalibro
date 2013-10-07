package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.RepositoryListener;
import org.kalibro.core.persistence.record.RepositoryListenerRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryListenerDatabaseDao.class)
public class RepositoryListenerDatabaseDaoTest extends
	DatabaseDaoTestCase<RepositoryListener, RepositoryListenerRecord, RepositoryListenerDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Test
	public void shouldGetListenersOfRepository() {
		assertDeepEquals(set(entity), dao.listenersOf(REPOSITORY_ID));
		verify(dao).createRecordQuery("repositoryListener.repository = :repository");
		verify(query).setParameter("repository", REPOSITORY_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, REPOSITORY_ID));

		verifyNew(RepositoryListenerRecord.class).withArguments(entity, REPOSITORY_ID);
		verify(dao).save(record);
	}

}