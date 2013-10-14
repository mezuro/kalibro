package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositorySubscriber;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.RepositorySubscriberDao;
import org.kalibro.service.xml.RepositorySubscriberXml;
import org.powermock.reflect.Whitebox;

public class RepositorySubscriberEndpointTest extends
	EndpointTest<RepositorySubscriber, RepositorySubscriberDao, RepositorySubscriberEndpoint> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Override
	protected RepositorySubscriber loadFixture() {
		Repository repository = new Repository("RepositoryEndpointTest name", GIT, "RepositoryEndpointTest address");
		Whitebox.setInternalState(repository, "id", REPOSITORY_ID);

		RepositorySubscriber repositorySubscriber = new RepositorySubscriber("RepositorySubscriberEndpointTest name",
			"RepositorySubscriberEndpointTest email");
		Whitebox.setInternalState(repositorySubscriber, "id", ID);
		return repositorySubscriber;
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.allRepositorySubscribers());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, port.saveRepositorySubscriber(new RepositorySubscriberXml(entity), REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		port.deleteRepositorySubscriber(ID);
		verify(dao).delete(ID);
	}

	@Test
	public void shouldGetSubscribersOf() {
		when(dao.subscribersOf(REPOSITORY_ID)).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.repositorySubscribersOf(REPOSITORY_ID));
	}
}
