package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.RepositoryListener;
import org.kalibro.dao.RepositoryListenerDao;
import org.kalibro.service.xml.RepositoryListenerXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(RepositoryListenerEndpointImpl.class)
public class RepositoryListenerEndpointImplTest
	extends EndpointImplementorTest<RepositoryListener, RepositoryListenerXml,
	RepositoryListenerDao, RepositoryListenerEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepEquals(list(xml), implementor.allRepositoryListeners());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveRepositoryListener(xml, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteRepositoryListener(ID);
		verify(dao).delete(ID);
	}

	@Test
	public void shouldGetListenersOf() {
		when(dao.listenersOf(REPOSITORY_ID)).thenReturn(sortedSet(entity));
		assertDeepEquals(list(xml), implementor.repositoryListenersOf(REPOSITORY_ID));
	}
}
