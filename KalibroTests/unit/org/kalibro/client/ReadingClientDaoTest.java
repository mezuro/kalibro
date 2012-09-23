package org.kalibro.client;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.service.ReadingEndpoint;
import org.kalibro.service.xml.ReadingXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ReadingClientDao.class)
public class ReadingClientDaoTest extends
	ClientTest<Reading, ReadingXml, ReadingXml, ReadingEndpoint, ReadingClientDao> {

	@Override
	protected Class<Reading> entityClass() {
		return Reading.class;
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(port.readingsOf(42L)).thenReturn(Arrays.asList(response));
		assertDeepList(client.readingsOf(42L), entity);
	}

	@Test
	public void shouldSave() {
		when(port.saveReading(request)).thenReturn(42L);
		assertEquals(42L, client.save(entity).longValue());
	}

	@Test
	public void shouldDelete() {
		client.delete(42L);
		verify(port).deleteReading(42L);
	}
}