package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.service.ReadingGroupEndpoint;
import org.kalibro.service.xml.ReadingGroupXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ReadingGroupClientDao.class)
public class ReadingGroupClientDaoTest extends
	ClientTest<ReadingGroup, ReadingGroupXml, ReadingGroupEndpoint, ReadingGroupClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<ReadingGroup> entityClass() {
		return ReadingGroup.class;
	}

	@Test
	public void shouldConfirmExistence() {
		when(port.readingGroupExists(ID)).thenReturn(true);
		assertFalse(client.exists(-1L));
		assertTrue(client.exists(ID));
	}

	@Test
	public void shouldGetById() {
		when(port.getReadingGroup(ID)).thenReturn(xml);
		assertSame(entity, client.get(ID));
	}

	@Test
	public void shouldGetAll() {
		when(port.allReadingGroups()).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveReadingGroup(xml)).thenReturn(ID);
		assertEquals(ID, client.save(entity));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteReadingGroup(ID);
	}
}