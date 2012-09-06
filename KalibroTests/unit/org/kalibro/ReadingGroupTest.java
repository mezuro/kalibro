package org.kalibro;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.dao.ReadingGroupDao;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, DaoFactory.class})
public class ReadingGroupTest extends TestCase {

	private ReadingGroup group;
	private ReadingGroupDao dao;

	@Before
	public void setUp() {
		dao = mock(ReadingGroupDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingGroupDao()).thenReturn(dao);
		group = spy(loadFixture("readingGroup-scholar", ReadingGroup.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByName() {
		assertSorted(new ReadingGroup("A"), new ReadingGroup("B"), new ReadingGroup("X"), new ReadingGroup("Z"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldImportFromFile() throws Exception {
		File file = mock(File.class);
		mockStatic(AbstractEntity.class);
		when(AbstractEntity.class, "importFrom", file, ReadingGroup.class).thenReturn(group);

		assertSame(group, ReadingGroup.importFrom(file));
		verifyPrivate(AbstractEntity.class).invoke("importFrom", file, ReadingGroup.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetAllReadings() {
		List<ReadingGroup> list = mock(List.class);
		when(dao.all()).thenReturn(list);
		assertSame(list, ReadingGroup.all());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultGroup() {
		group = new ReadingGroup();
		assertNull(group.getId());
		assertEquals("", group.getName());
		assertEquals("", group.getDescription());
		assertTrue(group.getReadings().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddReadingIfItDoesNotConflictWithExistentOnes() {
		Reading reading = mock(Reading.class);
		List<Reading> existents = group.getReadings();

		group.addReading(reading);
		assertTrue(group.getReadings().contains(reading));

		InOrder order = Mockito.inOrder(reading);
		for (Reading existent : existents)
			order.verify(reading).assertNoConflictWith(existent);
		order.verify(reading).setGroup(group);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveReading() {
		Reading reading = mock(Reading.class);
		group.addReading(reading);
		group.removeReading(reading);

		assertFalse(group.getReadings().contains(reading));
		verify(reading).setGroup(null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		group.save();
		verify(dao).save(group);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteIfExists() {
		group.delete();
		verify(dao, never()).delete(group);

		group.setId(42L);
		group.delete();
		verify(dao).delete(group);
	}
}