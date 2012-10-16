package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.LOCAL_DIRECTORY;

import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.persistence.record.RepositoryRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RepositoryDatabaseDao.class, RepositoryType.class})
public class RepositoryDatabaseDaoTest extends UnitTest {

	private static final Long ID = new Random().nextLong();
	private static final Long PROJECT_ID = new Random().nextLong();

	private Repository repository;
	private RepositoryRecord record;

	private RepositoryDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		repository = mock(Repository.class);
		record = mock(RepositoryRecord.class);
		whenNew(RepositoryRecord.class).withArguments(repository, PROJECT_ID).thenReturn(record);
		when(record.convert()).thenReturn(repository);
		when(record.id()).thenReturn(ID);
		dao = spy(new RepositoryDatabaseDao(null));
	}

	@Test
	public void shouldGetSupportedRepositoryTypes() {
		RepositoryType supported = LOCAL_DIRECTORY;
		RepositoryType notSupported = mock(RepositoryType.class);
		mockStatic(RepositoryType.class);
		when(RepositoryType.values()).thenReturn(new RepositoryType[]{supported, notSupported});

		assertDeepEquals(asSet(supported), dao.supportedTypes());
	}

	@Test
	public void shouldGetRepositoryOfProcessing() {
		TypedQuery<RepositoryRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("JOIN Processing processing WHERE processing.id = :processingId");
		when(query.getSingleResult()).thenReturn(record);

		assertSame(repository, dao.repositoryOf(ID));
		verify(query).setParameter("processingId", ID);
	}

	@Test
	public void shouldGetRepositoriesOfProject() {
		TypedQuery<RepositoryRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("WHERE repository.project.id = :projectId");
		when(query.getResultList()).thenReturn(asList(record));

		assertDeepEquals(asSet(repository), dao.repositoriesOf(PROJECT_ID));
		verify(query).setParameter("projectId", PROJECT_ID);
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(ID, dao.save(repository, PROJECT_ID));
		verify(dao).save(record);
	}
}