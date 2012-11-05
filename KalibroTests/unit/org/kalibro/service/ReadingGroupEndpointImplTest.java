package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.service.xml.ReadingGroupXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ReadingGroupEndpointImpl.class)
public class ReadingGroupEndpointImplTest extends
	EndpointImplementorTest<ReadingGroup, ReadingGroupXml, ReadingGroupDao, ReadingGroupEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(implementor.readingGroupExists(-1L));
		assertTrue(implementor.readingGroupExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(xml, implementor.getReadingGroup(ID));
	}

	@Test
	public void shouldGetAll() {
		doReturn(sortedSet(entity)).when(dao).all();
		assertDeepEquals(list(xml), implementor.allReadingGroups());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, implementor.saveReadingGroup(xml));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteReadingGroup(ID);
		verify(dao).delete(ID);
	}
}