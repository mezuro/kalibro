package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositoryObserver;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.RepositoryObserverDao;
import org.kalibro.service.xml.RepositoryObserverXml;
import org.powermock.reflect.Whitebox;

public class RepositoryObserverEndpointTest extends
	EndpointTest<RepositoryObserver, RepositoryObserverDao, RepositoryObserverEndpoint> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Override
	protected RepositoryObserver loadFixture() {
		Repository repository = new Repository("RepositoryEndpointTest name", GIT, "RepositoryEndpointTest address");
		Whitebox.setInternalState(repository, "id", REPOSITORY_ID);

		RepositoryObserver repositoryObserver = new RepositoryObserver("RepositoryObserverEndpointTest name",
			"RepositoryObserverEndpointTest email");
		Whitebox.setInternalState(repositoryObserver, "id", ID);
		return repositoryObserver;
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.allRepositoryObservers());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, port.saveRepositoryObserver(new RepositoryObserverXml(entity), REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		port.deleteRepositoryObserver(ID);
		verify(dao).delete(ID);
	}
}
