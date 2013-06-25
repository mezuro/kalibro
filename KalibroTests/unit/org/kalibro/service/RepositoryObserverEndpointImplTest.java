package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.RepositoryObserver;
import org.kalibro.dao.RepositoryObserverDao;
import org.kalibro.service.xml.RepositoryObserverXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(RepositoryObserverEndpointImpl.class)
public class RepositoryObserverEndpointImplTest
	extends EndpointImplementorTest<RepositoryObserver, RepositoryObserverXml,
	RepositoryObserverDao, RepositoryObserverEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepEquals(list(xml), implementor.allRepositoryObservers());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveRepositoryObserver(xml, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteRepositoryObserver(ID);
		verify(dao).delete(ID);
	}
}
