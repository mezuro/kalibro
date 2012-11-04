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
	private static final Long GROUP_ID = new Random().nextLong();

	@Override
	protected Reading loadFixture() {
		return loadFixture("excellent", Reading.class);
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getReading(ID));
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(GROUP_ID)).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.readingsOf(GROUP_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, GROUP_ID)).thenReturn(ID);
		assertEquals(ID, port.saveReading(new ReadingXml(entity), GROUP_ID));
	}

	@Test
	public void shouldDelete() {
		port.deleteReading(ID);
		verify(dao).delete(ID);
	}
}