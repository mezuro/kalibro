package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ReadingEndpointImpl.class)
public class ReadingEndpointImplTest extends
	EndpointImplementorTest<Reading, ReadingXml, ReadingXml, ReadingDao, ReadingEndpointImpl> {

	private static final Long ID = new Random().nextLong();
	private static final Long GROUP_ID = new Random().nextLong();

	@Override
	protected Class<Reading> entityClass() {
		return Reading.class;
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(response, implementor.getReading(ID));
	}

	@Test
	public void shouldGetReadingOfRange() {
		when(dao.readingOf(ID)).thenReturn(entity);
		assertSame(response, implementor.readingOf(ID));
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(GROUP_ID)).thenReturn(sortedSet(entity));
		assertDeepEquals(list(response), implementor.readingsOf(GROUP_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, GROUP_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveReading(request, GROUP_ID));
	}

	@Test
	public void shouldDeleteReading() {
		implementor.deleteReading(ID);
		verify(dao).delete(ID);
	}
}