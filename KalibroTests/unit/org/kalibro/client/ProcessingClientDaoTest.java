package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.service.ProcessingEndpoint;
import org.kalibro.service.xml.ProcessingXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ProcessingClientDao.class)
public class ProcessingClientDaoTest extends
	ClientTest<Processing, ProcessingXml, ProcessingEndpoint, ProcessingClientDao> {

	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());
	private static final Date DATE = new Date(new Random().nextLong());

	@Override
	protected Class<Processing> entityClass() {
		return Processing.class;
	}

	@Test
	public void shouldAnswerIfHasProcessing() {
		when(port.hasProcessing(REPOSITORY_ID)).thenReturn(true);
		assertFalse(client.hasProcessing(-1L));
		assertTrue(client.hasProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldAnswerIfHasReadyProcessing() {
		when(port.hasReadyProcessing(REPOSITORY_ID)).thenReturn(true);
		assertFalse(client.hasReadyProcessing(-1L));
		assertTrue(client.hasReadyProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldAnswerIfHasProcessingAfterDate() {
		when(port.hasProcessingAfter(DATE, REPOSITORY_ID)).thenReturn(true);
		assertFalse(client.hasProcessingAfter(DATE, -1L));
		assertTrue(client.hasProcessingAfter(DATE, REPOSITORY_ID));
	}

	@Test
	public void shouldAnswerIfHasProcessingBeforeDate() {
		when(port.hasProcessingBefore(DATE, REPOSITORY_ID)).thenReturn(true);
		assertFalse(client.hasProcessingBefore(DATE, -1L));
		assertTrue(client.hasProcessingBefore(DATE, REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastProcessingState() {
		ProcessState[] states = ProcessState.values();
		ProcessState state = states[new Random().nextInt(states.length)];
		when(port.lastProcessingState(REPOSITORY_ID)).thenReturn(state);
		assertSame(state, client.lastProcessingState(REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastReadyProcessing() {
		when(port.lastReadyProcessing(REPOSITORY_ID)).thenReturn(xml);
		assertSame(entity, client.lastReadyProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldGetFirstProcessing() {
		when(port.firstProcessing(REPOSITORY_ID)).thenReturn(xml);
		assertSame(entity, client.firstProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastProcessing() {
		when(port.lastProcessing(REPOSITORY_ID)).thenReturn(xml);
		assertSame(entity, client.lastProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldGetFirstProcessingAfterDate() {
		when(port.firstProcessingAfter(DATE, REPOSITORY_ID)).thenReturn(xml);
		assertSame(entity, client.firstProcessingAfter(DATE, REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastProcessingBeforeDate() {
		when(port.lastProcessingBefore(DATE, REPOSITORY_ID)).thenReturn(xml);
		assertSame(entity, client.lastProcessingBefore(DATE, REPOSITORY_ID));
	}
}