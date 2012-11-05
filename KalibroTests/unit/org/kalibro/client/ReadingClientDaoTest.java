package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.service.ReadingEndpoint;
import org.kalibro.service.xml.ReadingXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ReadingClientDao.class)
public class ReadingClientDaoTest extends ClientTest<Reading, ReadingXml, ReadingEndpoint, ReadingClientDao> {

	private static final Long ID = new Random().nextLong();
	private static final Long GROUP_ID = new Random().nextLong();

	@Override
	protected Class<Reading> entityClass() {
		return Reading.class;
	}

	@Test
	public void shouldGetById() {
		when(port.getReading(ID)).thenReturn(xml);
		assertSame(entity, client.get(ID));
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(port.readingsOf(GROUP_ID)).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.readingsOf(GROUP_ID));
	}

	@Test
	public void shouldSave() {
		when(port.saveReading(xml, GROUP_ID)).thenReturn(ID);
		assertEquals(ID, client.save(entity, GROUP_ID));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteReading(ID);
	}
}