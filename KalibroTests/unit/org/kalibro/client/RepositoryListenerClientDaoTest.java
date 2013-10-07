package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.RepositoryListener;
import org.kalibro.service.RepositoryListenerEndpoint;
import org.kalibro.service.xml.RepositoryListenerXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(RepositoryListenerClientDao.class)
public class RepositoryListenerClientDaoTest extends
	ClientTest<RepositoryListener, RepositoryListenerXml, RepositoryListenerEndpoint, RepositoryListenerClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<RepositoryListener> entityClass() {
		return RepositoryListener.class;
	}

	@Test
	public void shouldGetAllRepositoryListeners() {
		when(port.allRepositoryListeners()).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveRepositoryListener(xml, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteRepositoryListener(ID);
	}

	@Test
	public void shouldGetListenersOf() {
		when(port.repositoryListenersOf(REPOSITORY_ID)).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.listenersOf(REPOSITORY_ID));
	}
}
