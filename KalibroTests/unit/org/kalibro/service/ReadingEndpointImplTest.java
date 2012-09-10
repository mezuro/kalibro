package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.TestCase;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.kalibro.service.xml.ReadingXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, ReadingEndpointImpl.class})
public class ReadingEndpointImplTest extends TestCase {

	private Reading reading;
	private ReadingXml readingXml;

	private ReadingDao dao;
	private ReadingEndpointImpl endpoint;

	@Before
	public void setUp() throws Exception {
		mockReading();
		mockReadingDao();
		endpoint = new ReadingEndpointImpl();
	}

	private void mockReading() throws Exception {
		reading = mock(Reading.class);
		readingXml = mock(ReadingXml.class);
		when(readingXml.convert()).thenReturn(reading);
		whenNew(ReadingXml.class).withArguments(reading).thenReturn(readingXml);
	}

	private void mockReadingDao() {
		dao = mock(ReadingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReadingsOfGroup() {
		when(dao.readingsOf(42L)).thenReturn(Arrays.asList(reading));
		assertDeepList(endpoint.readingsOf(42L), readingXml);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveReading() {
		when(reading.getId()).thenReturn(42L);
		assertEquals(42L, endpoint.save(readingXml).longValue());
		verify(dao).save(reading);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteReading() {
		endpoint.delete(42L);
		verify(dao).delete(42L);
	}
}