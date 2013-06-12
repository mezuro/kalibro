package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ProcessingObserver;
import org.kalibro.dao.ProcessingObserverDao;
import org.kalibro.service.xml.ProcessingObserverXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ProcessingObserverEndpointImpl.class)
public class ProcessingObserverEndpointImplTest
	extends EndpointImplementorTest<ProcessingObserver, ProcessingObserverXml,
	ProcessingObserverDao, ProcessingObserverEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepEquals(list(xml), implementor.allProcessingObservers());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveProcessingObserver(xml, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteProcessingObserver(ID);
		verify(dao).delete(ID);
	}
}
