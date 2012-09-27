package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXmlRequest;
import org.kalibro.service.xml.ReadingGroupXmlResponse;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ReadingGroupEndpointImpl.class)
public class ReadingGroupEndpointImplTest extends EndpointImplementorTest<// @formatter:off
	ReadingGroup, ReadingGroupXmlRequest, ReadingGroupXmlResponse,
	ReadingGroupDao, ReadingGroupEndpointImpl> {// @formatter:on

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<ReadingGroup> entityClass() {
		return ReadingGroup.class;
	}

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(implementor.readingGroupExists(-1L));
		assertTrue(implementor.readingGroupExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(response, implementor.getReadingGroup(ID));
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(asSortedSet(entity));
		assertDeepEquals(asList(response), implementor.allReadingGroups());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, implementor.saveReadingGroup(request));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteReadingGroup(ID);
		verify(dao).delete(ID);
	}
}