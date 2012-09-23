package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

public class ReadingEndpointImplTest extends
	EndpointImplementorTest<Reading, ReadingXml, ReadingXml, ReadingDao, ReadingEndpointImpl> {

	@Override
	protected Class<Reading> entityClass() {
		return Reading.class;
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(42L)).thenReturn(Arrays.asList(entity));
		assertDeepList(implementor.readingsOf(42L), response);
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(42L);
		assertEquals(42L, implementor.saveReading(request).longValue());
	}

	@Test
	public void shouldDeleteReading() {
		implementor.deleteReading(42L);
		verify(dao).delete(42L);
	}
}