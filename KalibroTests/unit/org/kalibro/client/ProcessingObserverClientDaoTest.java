package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ProcessingObserver;
import org.kalibro.service.ProcessingObserverEndpoint;
import org.kalibro.service.xml.ProcessingObserverXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ProcessingObserverClientDao.class)
public class ProcessingObserverClientDaoTest extends
	ClientTest<ProcessingObserver, ProcessingObserverXml, ProcessingObserverEndpoint, ProcessingObserverClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<ProcessingObserver> entityClass() {
		return ProcessingObserver.class;
	}

	@Test
	public void shouldGetAllProcessingObservers() {
		when(port.allProcessingObservers()).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveProcessingObserver(xml, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteProcessingObserver(ID);
	}

}
