package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;
import org.powermock.reflect.Whitebox;

public class ReadingGroupEndpointTest extends EndpointTest {

	private ReadingGroup fixture;
	private ReadingGroupDao dao;

	private ReadingGroupEndpoint port;

	@Before
	public void setUp() {
		fixture = loadFixture("scholar", ReadingGroup.class);
		fixture.setId(0L);
		dao = mock(ReadingGroupDao.class);
		port = publishAndGetPort(new ReadingGroupEndpointImpl(dao), ReadingGroupEndpoint.class);
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(Arrays.asList(fixture));

		List<ReadingGroupXmlResponse> groups = port.allReadingGroups();
		assertEquals(1, groups.size());
		assertCorrectResponse(groups.get(0));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(42L)).thenReturn(fixture);
		assertCorrectResponse(port.getReadingGroup(42L));
	}

	private void assertCorrectResponse(ReadingGroupXmlResponse response) {
		ReadingGroup group = response.convert();
		readingsShouldBeProxy(group);
		assertDeepEquals(fixture, group);
	}

	private void readingsShouldBeProxy(ReadingGroup group) {
		List<Reading> readings = Whitebox.getInternalState(group, "readings");
		assertTrue(readings.getClass().getName().contains("EnhancerByCGLIB"));
		group.setReadings(fixture.getReadings());
	}

	@Test
	public void shouldSave() {
		when(dao.save(fixture)).thenReturn(42L);
		assertEquals(42L, port.saveReadingGroup(new ReadingGroupXmlRequest(fixture)).longValue());
	}

	@Test
	public void shouldDelete() {
		port.deleteReadingGroup(42L);
		verify(dao).delete(42L);
	}
}