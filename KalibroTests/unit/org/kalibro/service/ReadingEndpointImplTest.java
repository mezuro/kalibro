package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ReadingEndpointImpl.class)
public class ReadingEndpointImplTest extends
	EndpointImplementorTest<Reading, ReadingXml, ReadingXml, ReadingDao, ReadingEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<Reading> entityClass() {
		return Reading.class;
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(ID)).thenReturn(asList(entity));
		assertDeepEquals(asList(response), implementor.readingsOf(ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, implementor.saveReading(request));
	}

	@Test
	public void shouldDeleteReading() {
		implementor.deleteReading(ID);
		verify(dao).delete(ID);
	}
}