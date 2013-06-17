package org.kalibro.service;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ProcessingObserver;
import org.kalibro.Repository;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ProcessingObserverDao;
import org.kalibro.service.xml.ProcessingObserverXml;
import org.powermock.reflect.Whitebox;

public class ProcessingObserverEndpointTest extends
	EndpointTest<ProcessingObserver, ProcessingObserverDao, ProcessingObserverEndpoint> {

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();

	@Override
	protected ProcessingObserver loadFixture() {
		Repository repository = new Repository("RepositoryEndpointTest name", GIT, "RepositoryEndpointTest address");
		Whitebox.setInternalState(repository, "id", REPOSITORY_ID);

		ProcessingObserver processingObserver = new ProcessingObserver("ProcessingObserverEndpointTest name",
			"ProcessingObserverEndpointTest email");
		Whitebox.setInternalState(processingObserver, "id", ID);
		return processingObserver;
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.allProcessingObservers());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, REPOSITORY_ID)).thenReturn(ID);
		assertEquals(ID, port.saveProcessingObserver(new ProcessingObserverXml(entity), REPOSITORY_ID));
	}

	@Test
	public void shouldDelete() {
		port.deleteProcessingObserver(ID);
		verify(dao).delete(ID);
	}
}
