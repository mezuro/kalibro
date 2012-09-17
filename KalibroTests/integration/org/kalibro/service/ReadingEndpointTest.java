package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;

public class ReadingEndpointTest extends EndpointTest {

	private Reading fixture;
	private ReadingDao dao;

	private ReadingEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		fixture = loadFixture("/org/kalibro/reading-excellent", Reading.class);
		dao = mock(ReadingDao.class);
		port = publishAndGetPort(new ReadingEndpointImpl(dao), ReadingEndpoint.class);
	}

	@Test
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(42L)).thenReturn(Arrays.asList(fixture));

		List<ReadingXml> readings = port.readingsOf(42L);
		assertEquals(1, readings.size());
		assertDeepEquals(fixture, readings.get(0).convert());
	}

	@Test
	public void shouldSave() {
		when(dao.save(eq(fixture))).thenReturn(42L);
		assertEquals(42L, port.saveReading(new ReadingXml(fixture)).longValue());
	}

	@Test
	public void shouldDelete() {
		port.deleteReading(42L);
		verify(dao).delete(42L);
	}
}