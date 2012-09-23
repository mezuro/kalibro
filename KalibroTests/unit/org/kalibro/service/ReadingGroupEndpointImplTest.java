package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;

public class ReadingGroupEndpointImplTest extends EndpointImplementorTest<// @formatter:off
	ReadingGroup, ReadingGroupXmlRequest, ReadingGroupXmlResponse,
	ReadingGroupDao, ReadingGroupEndpointImpl> {// @formatter:on

	@Override
	protected Class<ReadingGroup> entityClass() {
		return ReadingGroup.class;
	}

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(42L)).thenReturn(true);
		assertFalse(implementor.readingGroupExists(28L));
		assertTrue(implementor.readingGroupExists(42L));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(42L)).thenReturn(entity);
		assertSame(response, implementor.getReadingGroup(42L));
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(Arrays.asList(entity));
		assertDeepList(implementor.allReadingGroups(), response);
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(42L);
		assertEquals(42L, implementor.saveReadingGroup(request).longValue());
	}

	@Test
	public void shouldDelete() {
		implementor.deleteReadingGroup(42L);
		verify(dao).delete(42L);
	}
}