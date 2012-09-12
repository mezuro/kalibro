package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

public class ReadingEndpointImplTest extends
	EndpointImplementationTest<Reading, ReadingXml, ReadingXml, ReadingDao, ReadingEndpointImpl> {

	@Override
	protected Class<?>[] parameterClasses() {
		return new Class<?>[]{
			Reading.class, ReadingXml.class, ReadingXml.class, ReadingDao.class, ReadingEndpointImpl.class};
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(42L)).thenReturn(Arrays.asList(entity));
		assertDeepList(endpoint.readingsOf(42L), response);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(42L);
		assertEquals(42L, endpoint.saveReading(request).longValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteReading() {
		endpoint.deleteReading(42L);
		verify(dao).delete(42L);
	}
}