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
	EndpointImplementorTest<Reading, ReadingXml, ReadingDao, ReadingEndpointImpl> {

	private static final Long ID = new Random().nextLong();
	private static final Long GROUP_ID = new Random().nextLong();

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(xml, implementor.getReading(ID));
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		doReturn(sortedSet(entity)).when(dao).readingsOf(GROUP_ID);
		assertDeepEquals(list(xml), implementor.readingsOf(GROUP_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, GROUP_ID)).thenReturn(ID);
		assertEquals(ID, implementor.saveReading(xml, GROUP_ID));
	}

	@Test
	public void shouldDeleteReading() {
		implementor.deleteReading(ID);
		verify(dao).delete(ID);
	}
}