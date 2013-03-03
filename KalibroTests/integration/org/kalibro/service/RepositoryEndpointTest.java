package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.Repository;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.service.xml.RepositoryXml;
import org.powermock.reflect.Whitebox;

public class RepositoryEndpointTest extends EndpointTest<Repository, RepositoryDao, RepositoryEndpoint> {

	private static final Long ID = new Random().nextLong();
	private static final Long PROJECT_ID = new Random().nextLong();

	@Override
	public Repository loadFixture() {
		Configuration configuration = new Configuration();
		Whitebox.setInternalState(configuration, "id", ID);

		Repository repository = new Repository("RepositoryEndpointTest name", GIT, "RepositoryEndpointTest address");
		Whitebox.setInternalState(repository, "id", ID);

		repository.setConfiguration(configuration);
		return repository;
	}

	@Override
	public List<String> fieldsThatShouldBeProxy() {
		return list("configuration");
	}

	@Test
	public void shouldGetSupportedTypes() {
		when(dao.supportedTypes()).thenReturn(sortedSet(GIT, LOCAL_DIRECTORY));
		assertDeepEquals(list(GIT), port.supportedRepositoryTypes());
	}

	@Test
	public void shouldGetRepositoriesOfProject() {
		when(dao.repositoriesOf(PROJECT_ID)).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.repositoriesOf(PROJECT_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, PROJECT_ID)).thenReturn(ID);
		assertEquals(ID, port.saveRepository(new RepositoryXml(entity), PROJECT_ID));
	}

	@Test
	public void shouldProcess() {
		port.processRepository(ID);
		verify(dao).process(ID);
	}

	@Test
	public void shouldCancelProcessing() {
		port.cancelProcessingOfRepository(ID);
		verify(dao).cancelProcessing(ID);
	}

	@Test
	public void shouldDelete() {
		port.deleteRepository(ID);
		verify(dao).delete(ID);
	}
}