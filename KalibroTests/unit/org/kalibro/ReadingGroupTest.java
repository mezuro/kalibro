package org.kalibro;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.concurrent.Task;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dao.ReadingGroupDao;
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
		group = loadFixture("readingGroup-scholar", ReadingGroup.class);
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
		assertFalse(group.hasId());
		assertEquals("", group.getName());
		assertEquals("", group.getDescription());
		assertTrue(group.getReadings().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetGroupOnReadings() {
		Reading reading = mock(Reading.class);
		group.setReadings(Arrays.asList(reading));
		assertDeepList(group.getReadings(), reading);
		verify(reading).setGroup(group);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetReadingsWithoutTouchingThem() {
		// required for lazy loading
		List<Reading> readings = mock(List.class);
		group.setReadings(readings);
		verifyZeroInteractions(readings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddReadingIfItDoesNotConflictWithExistingOnes() {
		Reading reading = mock(Reading.class);
		List<Reading> existents = group.getReadings();
		group.addReading(reading);

		InOrder order = Mockito.inOrder(reading);
		for (Reading existent : existents)
			order.verify(reading).assertNoConflictWith(existent);
		order.verify(reading).setGroup(group);

		assertTrue(group.getReadings().contains(reading));
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
	public void shouldUpdateIdAndReadingsOnSave() {
		Reading reading = mock(Reading.class);
		ReadingDao readingDao = mock(ReadingDao.class);
		when(dao.save(group)).thenReturn(42L);
		when(DaoFactory.getReadingDao()).thenReturn(readingDao);
		when(readingDao.readingsOf(42L)).thenReturn(Arrays.asList(reading));

		assertFalse(group.hasId());
		group.save();
		assertEquals(42L, group.getId().longValue());
		assertDeepList(group.getReadings(), reading);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRequiredNameToSave() {
		group.setName(" ");
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				group.save();
			}
		}, "Reading group requires name.");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDeleteIfHasId() {
		assertFalse(group.hasId());
		group.delete();
		verify(dao, never()).delete(any(Long.class));

		group.setId(42L);

		assertTrue(group.hasId());
		group.delete();
		verify(dao).delete(42L);
		assertFalse(group.hasId());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveReadingIdsOnDelete() {
		Reading reading = mock(Reading.class);
		group.setReadings(Arrays.asList(reading));
		group.setId(42L);

		group.delete();
		verify(reading).setId(null);
	}
}