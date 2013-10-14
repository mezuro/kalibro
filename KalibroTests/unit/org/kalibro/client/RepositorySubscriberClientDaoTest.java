package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.RepositorySubscriber;
import org.kalibro.service.RepositorySubscriberEndpoint;
import org.kalibro.service.xml.RepositorySubscriberXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(RepositorySubscriberClientDao.class)
public class RepositorySubscriberClientDaoTest extends ClientTest
	<RepositorySubscriber, RepositorySubscriberXml, RepositorySubscriberEndpoint, RepositorySubscriberClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<RepositorySubscriber> entityClass() {
		return RepositorySubscriber.class;
	}

	@Test
	public void shouldGetAllRepositorySubscribers() {
		when(port.allRepositorySubscribers()).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveRepositorySubscriber(xml, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteRepositorySubscriber(ID);
	}

	@Test
	public void shouldGetSubscribersOf() {
		when(port.repositorySubscribersOf(REPOSITORY_ID)).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.subscribersOf(REPOSITORY_ID));
	}
}
