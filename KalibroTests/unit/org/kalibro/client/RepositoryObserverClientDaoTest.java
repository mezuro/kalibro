package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.RepositoryObserver;
import org.kalibro.service.RepositoryObserverEndpoint;
import org.kalibro.service.xml.RepositoryObserverXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(RepositoryObserverClientDao.class)
public class RepositoryObserverClientDaoTest extends
	ClientTest<RepositoryObserver, RepositoryObserverXml, RepositoryObserverEndpoint, RepositoryObserverClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<RepositoryObserver> entityClass() {
		return RepositoryObserver.class;
	}

	@Test
	public void shouldGetAllRepositoryObservers() {
		when(port.allRepositoryObservers()).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveRepositoryObserver(xml, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteRepositoryObserver(ID);
	}

}
