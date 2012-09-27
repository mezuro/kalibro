package org.kalibro.client;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.service.ReadingEndpoint;
import org.kalibro.service.xml.ReadingXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ReadingClientDao.class)
public class ReadingClientDaoTest extends
	ClientTest<Reading, ReadingXml, ReadingXml, ReadingEndpoint, ReadingClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<Reading> entityClass() {
		return Reading.class;
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(port.readingsOf(ID)).thenReturn(asList(response));
		assertDeepEquals(asSet(entity), client.readingsOf(ID));
	}

	@Test
	public void shouldSave() {
		when(port.saveReading(request)).thenReturn(ID);
		assertEquals(ID, client.save(entity));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteReading(ID);
	}
}