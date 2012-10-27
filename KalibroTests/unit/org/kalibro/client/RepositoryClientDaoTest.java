package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.service.RepositoryEndpoint;
import org.kalibro.service.xml.RepositoryXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(RepositoryClientDao.class)
public class RepositoryClientDaoTest extends ClientTest<// @formatter:off
	Repository, RepositoryXml, RepositoryXml, RepositoryEndpoint, RepositoryClientDao> {// @formatter:on

	private static final Long ID = new Random().nextLong();
	private static final Long PROJECT_ID = new Random().nextLong();

	@Override
	protected Class<Repository> entityClass() {
		return Repository.class;
	}

	@Test
	public void shouldGetSupportedTypes() {
		RepositoryType[] types = RepositoryType.values();
		RepositoryType supportedType = types[new Random().nextInt(types.length)];
		when(port.supportedRepositoryTypes()).thenReturn(list(supportedType));
		assertDeepEquals(set(supportedType), client.supportedTypes());
	}

	@Test
	public void shouldGetRepositoryOfProcessing() {
		when(port.repositoryOf(ID)).thenReturn(response);
		assertSame(entity, client.repositoryOf(ID));
	}

	@Test
	public void shouldGetRepositoriesOfProject() {
		when(port.repositoriesOf(PROJECT_ID)).thenReturn(list(response));
		assertDeepEquals(set(entity), client.repositoriesOf(PROJECT_ID));
	}

	@Test
	public void shouldSave() {
		when(port.saveRepository(request, PROJECT_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, PROJECT_ID));
	}

	@Test
	public void shouldProcess() {
		client.process(ID);
		verify(port).processRepository(ID);
	}

	@Test
	public void shouldCancelProcessing() {
		client.cancelProcessing(ID);
		verify(port).cancelProcessingOfRepository(ID);
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteRepository(ID);
	}
}