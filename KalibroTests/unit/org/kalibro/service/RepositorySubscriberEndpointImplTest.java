package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.RepositorySubscriber;
import org.kalibro.dao.RepositorySubscriberDao;
import org.kalibro.service.xml.RepositorySubscriberXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(RepositorySubscriberEndpointImpl.class)
public class RepositorySubscriberEndpointImplTest
	extends EndpointImplementorTest<RepositorySubscriber, RepositorySubscriberXml,
	RepositorySubscriberDao, RepositorySubscriberEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepEquals(list(xml), implementor.allRepositorySubscribers());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveRepositorySubscriber(xml, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteRepositorySubscriber(ID);
		verify(dao).delete(ID);
	}

	@Test
	public void shouldGetSubscribersOf() {
		when(dao.subscribersOf(REPOSITORY_ID)).thenReturn(sortedSet(entity));
		assertDeepEquals(list(xml), implementor.repositorySubscribersOf(REPOSITORY_ID));
	}
}
