package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.service.xml.RepositoryXmlRequest;
import org.kalibro.service.xml.RepositoryXmlResponse;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({RepositoryEndpointImpl.class, RepositoryType.class})
public class RepositoryEndpointImplTest extends EndpointImplementorTest<// @formatter:off
	Repository, RepositoryXmlRequest, RepositoryXmlResponse, RepositoryDao, RepositoryEndpointImpl> {// @formatter:on

	private static final Long ID = new Random().nextLong();
	private static final Long PROJECT_ID = new Random().nextLong();

	@Override
	protected Class<Repository> entityClass() {
		return Repository.class;
	}

	@Test
	public void shouldGetSupportedTypes() {
		RepositoryType supportedType = mock(RepositoryType.class);
		when(dao.supportedTypes()).thenReturn(asSortedSet(supportedType));
		assertDeepEquals(asList(supportedType), implementor.supportedRepositoryTypes());
	}

	@Test
	public void shouldGetRepositoryOfProcessing() {
		when(dao.repositoryOf(ID)).thenReturn(entity);
		assertSame(response, implementor.repositoryOf(ID));
	}

	@Test
	public void shouldGetRepositoriesOfProject() {
		when(dao.repositoriesOf(PROJECT_ID)).thenReturn(asSortedSet(entity));
		assertDeepEquals(asList(response), implementor.repositoriesOf(PROJECT_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, PROJECT_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveRepository(request, PROJECT_ID));
	}

	@Test
	public void shouldProcess() {
		implementor.processRepository(ID);
		verify(dao).process(ID);
	}

	@Test
	public void shouldCancelProcessing() {
		implementor.cancelProcessingOfRepository(ID);
		verify(dao).cancelProcessing(ID);
	}

	@Test
	public void shouldDelete() {
		implementor.deleteRepository(ID);
		verify(dao).delete(ID);
	}
}