package org.kalibro;

import static org.junit.Assert.*;

import java.awt.Color;
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
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	@Test
	public void shouldIdentifyByName() {
		assertEquals(group, withName(group.getName()));
	}

	private ReadingGroup withName(String name) {
		return new ReadingGroup(name);
	}

	@Test
	public void checkConstruction() {
		group = new ReadingGroup();
		assertFalse(group.hasId());
		assertEquals("", group.getName());
		assertEquals("", group.getDescription());
		assertTrue(group.getReadings().isEmpty());
	}

	@Test
	public void shouldSetGroupOnReadings() {
		Reading reading = mock(Reading.class);
		group.setReadings(sortedSet(reading));
		assertDeepEquals(set(reading), group.getReadings());
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
		group.addReading(new Reading("label", 42.0, Color.WHITE));
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				group.addReading(new Reading());
			}
		}).throwsException().withMessage("Reading with grade 0.0 already exists in the group.");
	}

	@Test
	public void shouldRemoveReading() {
		Reading reading = mock(Reading.class);
		SortedSet<Reading> readings = spy(sortedSet(reading));
		group.setReadings(readings);

		group.removeReading(reading);
		verify(readings).remove(reading);
		verify(reading).setGroup(null);
	}

	@Test
	public void shouldUpdateIdAndReadingsOnSave() {
		Long id = mock(Long.class);
		Reading reading = mockReading(id);
		when(dao.save(group)).thenReturn(id);

		assertFalse(group.hasId());
		group.save();
		assertSame(id, group.getId());
		assertDeepEquals(set(reading), group.getReadings());
	}

	private Reading mockReading(Long id) {
		Reading reading = mock(Reading.class);
		ReadingDao readingDao = mock(ReadingDao.class);
		when(DaoFactory.getReadingDao()).thenReturn(readingDao);
		when(readingDao.readingsOf(id)).thenReturn(sortedSet(reading));
		return reading;
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
		group.setReadings(sortedSet(reading));
		Whitebox.setInternalState(group, "id", 42L);

		group.delete();
		verify(reading).deleted();
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(group.getName(), "" + group);
	}
}