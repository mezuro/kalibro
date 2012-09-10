package org.kalibro.client;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.TestCase;
import org.kalibro.service.ReadingEndpoint;
import org.kalibro.service.xml.ReadingXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReadingClientDao.class, EndpointClient.class})
public class ReadingPortDaoTest extends TestCase {

	private Reading reading;
	private ReadingXml readingXml;

	private ReadingClientDao dao;
	private ReadingEndpoint port;

	@Before
	public void setUp() throws Exception {
		mockReading();
		createSupressedDao();
	}

	private void mockReading() throws Exception {
		reading = mock(Reading.class);
		readingXml = mock(ReadingXml.class);
		when(readingXml.convert()).thenReturn(reading);
		whenNew(ReadingXml.class).withArguments(reading).thenReturn(readingXml);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new ReadingClientDao("");

		port = mock(ReadingEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReadingsOfGroup() {
		when(port.readingsOf(42L)).thenReturn(Arrays.asList(readingXml));
		assertDeepList(dao.readingsOf(42L), reading);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveAndMerge() {
		when(port.save(readingXml)).thenReturn(42L);
		dao.save(reading);
		verify(reading).setId(42L);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteReading() {
		dao.delete(42L);
		verify(port).delete(42L);
	}
}