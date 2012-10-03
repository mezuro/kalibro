package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, DaoFactory.class})
public class ReadingGroupTest extends UnitTest {

	private ReadingGroupDao dao;
	private ReadingGroup group;

	@Before
	public void setUp() {
		dao = mock(ReadingGroupDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingGroupDao()).thenReturn(dao);
		group = loadFixture("scholar", ReadingGroup.class);
	}

	@Test
	public void shouldSortByName() {
		assertSorted(new ReadingGroup("A"), new ReadingGroup("B"), new ReadingGroup("X"), new ReadingGroup("Z"));
	}

	@Test
	public void shouldImportFromFile() throws Exception {
		File file = mock(File.class);
		mockStatic(AbstractEntity.class);
		when(AbstractEntity.class, "importFrom", file, ReadingGroup.class).thenReturn(group);

		assertSame(group, ReadingGroup.importFrom(file));
		verifyPrivate(AbstractEntity.class).invoke("importFrom", file, ReadingGroup.class);
	}

	@Test
	public void shouldGetAllReadingGroups() {
		SortedSet<ReadingGroup> groups = mock(SortedSet.class);
		when(dao.all()).thenReturn(groups);
		assertSame(groups, ReadingGroup.all());
	}

	@Test
	public void checkDefaultGroup() {
		group = new ReadingGroup();
		assertFalse(group.hasId());
		assertEquals("", group.getName());
		assertEquals("", group.getDescription());
		assertTrue(group.getReadings().isEmpty());
	}

	@Test
	public void shouldSetGroupOnReadings() {
		Reading reading = mock(Reading.class);
		group.setReadings(asSortedSet(reading));
		assertDeepEquals(asSet(reading), group.getReadings());
		verify(reading).setGroup(group);
	}

	@Test
	public void shouldSetReadingsWithoutTouchingThem() {
		// required for lazy loading
		SortedSet<Reading> readings = mock(SortedSet.class);
		group.setReadings(readings);
		verifyZeroInteractions(readings);
	}

	@Test
	public void shouldAddReadingIfItDoesNotConflictWithExistingOnes() {
		Reading reading = mock(Reading.class);
		SortedSet<Reading> existents = group.getReadings();
		group.addReading(reading);

		InOrder order = Mockito.inOrder(reading);
		for (Reading existent : existents)
			order.verify(reading).assertNoConflictWith(existent);
		order.verify(reading).setGroup(group);

		assertTrue(group.getReadings().contains(reading));
	}

	@Test
	public void shouldRemoveReading() {
		Reading reading = mock(Reading.class);
		SortedSet<Reading> readings = spy(asSortedSet(reading));
		group.setReadings(readings);

		group.removeReading(reading);
		verify(readings).remove(reading);
		verify(reading).setGroup(null);
	}

	@Test
	public void shouldUpdateIdAndReadingsOnSave() {
		Reading reading = mock(Reading.class);
		ReadingDao readingDao = mock(ReadingDao.class);
		when(dao.save(group)).thenReturn(42L);
		when(DaoFactory.getReadingDao()).thenReturn(readingDao);
		when(readingDao.readingsOf(42L)).thenReturn(asSortedSet(reading));

		assertFalse(group.hasId());
		group.save();
		assertEquals(42L, group.getId().longValue());
		assertDeepEquals(asSet(reading), group.getReadings());
	}

	@Test
	public void shouldRequiredNameToSave() {
		group.setName(" ");
		assertThat(save()).throwsException().withMessage("Reading group requires name.");
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			protected void perform() {
				group.save();
			}
		};
	}

	@Test
	public void shouldDeleteIfHasId() {
		assertFalse(group.hasId());
		group.delete();
		verify(dao, never()).delete(any(Long.class));

		Long id = mock(Long.class);
		Whitebox.setInternalState(group, "id", id);

		assertTrue(group.hasId());
		group.delete();
		verify(dao).delete(id);
		assertFalse(group.hasId());
	}

	@Test
	public void shouldNotifyReadingsOfDeletion() {
		Reading reading = mock(Reading.class);
		group.setReadings(asSortedSet(reading));
		group.setId(42L);

		group.delete();
		verify(reading).deleted();
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(group.getName(), "" + group);
	}
}