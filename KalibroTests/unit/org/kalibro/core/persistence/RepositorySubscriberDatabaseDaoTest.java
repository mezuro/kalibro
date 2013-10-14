package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.RepositorySubscriber;
import org.kalibro.core.persistence.record.RepositorySubscriberRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositorySubscriberDatabaseDao.class)
public class RepositorySubscriberDatabaseDaoTest extends
	DatabaseDaoTestCase<RepositorySubscriber, RepositorySubscriberRecord, RepositorySubscriberDatabaseDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Test
	public void shouldGetSubscribersOfRepository() {
		assertDeepEquals(set(entity), dao.subscribersOf(REPOSITORY_ID));
		verify(dao).createRecordQuery("repositorySubscriber.repository = :repository");
		verify(query).setParameter("repository", REPOSITORY_ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity, REPOSITORY_ID));

		verifyNew(RepositorySubscriberRecord.class).withArguments(entity, REPOSITORY_ID);
		verify(dao).save(record);
	}

}