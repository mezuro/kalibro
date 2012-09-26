package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

public class ReadingEndpointTest extends EndpointTest<Reading, ReadingDao, ReadingEndpoint> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected Reading loadFixture() {
		return loadFixture("excellent", Reading.class);
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(ID)).thenReturn(asList(entity));
		assertDeepDtoList(port.readingsOf(ID), entity);
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, port.saveReading(new ReadingXml(entity)));
	}

	@Test
	public void shouldDelete() {
		port.deleteReading(ID);
		verify(dao).delete(ID);
	}
}