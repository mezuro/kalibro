package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;

public class ReadingGroupEndpointTest extends EndpointTest<ReadingGroup, ReadingGroupDao, ReadingGroupEndpoint> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	public ReadingGroup loadFixture() {
		ReadingGroup fixture = loadFixture("scholar", ReadingGroup.class);
		fixture.setId(ID);
		return fixture;
	}

	@Override
	public List<String> fieldsThatShouldBeProxy() {
		return asList("readings");
	}

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(port.readingGroupExists(-1L));
		assertTrue(port.readingGroupExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getReadingGroup(ID));
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(asSortedSet(entity));
		assertDeepDtoList(port.allReadingGroups(), entity);
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, port.saveReadingGroup(new ReadingGroupXmlRequest(entity)));
	}

	@Test
	public void shouldDelete() {
		port.deleteReadingGroup(ID);
		verify(dao).delete(ID);
	}
}