package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

public class ReadingEndpointTest extends EndpointTest<Reading, ReadingDao, ReadingEndpoint> {

	@Override
	protected Reading loadFixture() {
		return loadFixture("excellent", Reading.class);
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(42L)).thenReturn(Arrays.asList(entity));
		assertDeepDtoList(port.readingsOf(42L), entity);
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(42L);
		assertEquals(42L, port.saveReading(new ReadingXml(entity)).longValue());
	}

	@Test
	public void shouldDelete() {
		port.deleteReading(42L);
		verify(dao).delete(42L);
	}
}