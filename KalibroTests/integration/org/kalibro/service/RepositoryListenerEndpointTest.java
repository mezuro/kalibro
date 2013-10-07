package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositoryListener;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.RepositoryListenerDao;
import org.kalibro.service.xml.RepositoryListenerXml;
import org.powermock.reflect.Whitebox;

public class RepositoryListenerEndpointTest extends
	EndpointTest<RepositoryListener, RepositoryListenerDao, RepositoryListenerEndpoint> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Override
	protected RepositoryListener loadFixture() {
		Repository repository = new Repository("RepositoryEndpointTest name", GIT, "RepositoryEndpointTest address");
		Whitebox.setInternalState(repository, "id", REPOSITORY_ID);

		RepositoryListener repositoryListener = new RepositoryListener("RepositoryListenerEndpointTest name",
			"RepositoryListenerEndpointTest email");
		Whitebox.setInternalState(repositoryListener, "id", ID);
		return repositoryListener;
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.allRepositoryListeners());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, port.saveRepositoryListener(new RepositoryListenerXml(entity), REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		port.deleteRepositoryListener(ID);
		verify(dao).delete(ID);
	}

	@Test
	public void shouldGetListenersOf() {
		when(dao.listenersOf(REPOSITORY_ID)).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.repositoryListenersOf(REPOSITORY_ID));
	}
}
