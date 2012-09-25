package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;

public class ReadingGroupEndpointTest extends EndpointTest<ReadingGroup, ReadingGroupDao, ReadingGroupEndpoint> {

	@Override
	public ReadingGroup loadFixture() {
		ReadingGroup fixture = loadFixture("scholar", ReadingGroup.class);
		fixture.setId(28L);
		return fixture;
	}

	@Override
	public List<String> fieldsThatShouldBeProxy() {
		return Arrays.asList("readings");
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(Arrays.asList(entity));
		assertDeepDtoList(port.allReadingGroups(), entity);
	}

	@Test
	public void shouldGetById() {
		when(dao.get(42L)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getReadingGroup(42L));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(42L);
		assertEquals(42L, port.saveReadingGroup(new ReadingGroupXmlRequest(entity)).longValue());
	}

	@Test
	public void shouldDelete() {
		port.deleteReadingGroup(42L);
		verify(dao).delete(42L);
	}
}