package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.service.xml.ProcessingXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({ProcessingEndpointImpl.class, ProcessState.class})
public class ProcessingEndpointImplTest extends
	EndpointImplementorTest<Processing, ProcessingXml, ProcessingDao, ProcessingEndpointImpl> {

	private static final Long REPOSITORY_ID = Math.abs(new Random().nextLong());
	private static final Date DATE = new Date(new Random().nextLong());

	@Override
	protected Class<Processing> entityClass() {
		return Processing.class;
	}

	@Test
	public void shouldAnswerIfHasProcessing() {
		when(dao.hasProcessing(REPOSITORY_ID)).thenReturn(true);
		assertFalse(implementor.hasProcessing(-1L));
		assertTrue(implementor.hasProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldAnswerIfHasReadyProcessing() {
		when(dao.hasReadyProcessing(REPOSITORY_ID)).thenReturn(true);
		assertFalse(implementor.hasReadyProcessing(-1L));
		assertTrue(implementor.hasReadyProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldAnswerIfHasProcessingAfterDate() {
		when(dao.hasProcessingAfter(DATE, REPOSITORY_ID)).thenReturn(true);
		assertFalse(implementor.hasProcessingAfter(DATE, -1L));
		assertTrue(implementor.hasProcessingAfter(DATE, REPOSITORY_ID));
	}

	@Test
	public void shouldAnswerIfHasProcessingBeforeDate() {
		when(dao.hasProcessingBefore(DATE, REPOSITORY_ID)).thenReturn(true);
		assertFalse(implementor.hasProcessingBefore(DATE, -1L));
		assertTrue(implementor.hasProcessingBefore(DATE, REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastProcessingState() {
		ProcessState state = mock(ProcessState.class);
		when(dao.lastProcessingState(REPOSITORY_ID)).thenReturn(state);
		assertSame(state, implementor.lastProcessingState(REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastReadyProcessing() {
		when(dao.lastReadyProcessing(REPOSITORY_ID)).thenReturn(entity);
		assertSame(xml, implementor.lastReadyProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldGetFirstProcessing() {
		when(dao.firstProcessing(REPOSITORY_ID)).thenReturn(entity);
		assertSame(xml, implementor.firstProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastProcessing() {
		when(dao.lastProcessing(REPOSITORY_ID)).thenReturn(entity);
		assertSame(xml, implementor.lastProcessing(REPOSITORY_ID));
	}

	@Test
	public void shouldGetFirstProcessingAfterDate() {
		when(dao.firstProcessingAfter(DATE, REPOSITORY_ID)).thenReturn(entity);
		assertSame(xml, implementor.firstProcessingAfter(DATE, REPOSITORY_ID));
	}

	@Test
	public void shouldGetLastProcessingBeforeDate() {
		when(dao.lastProcessingBefore(DATE, REPOSITORY_ID)).thenReturn(entity);
		assertSame(xml, implementor.lastProcessingBefore(DATE, REPOSITORY_ID));
	}
}