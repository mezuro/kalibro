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
	public void shouldAssertSaved() {
		prepareSave(42L);

		group.assertSaved();
		verify(dao).save(group);

		group.assertSaved();
		verifyNoMoreInteractions(dao);
	}

	@Test
	public void shouldRequiredNameToSave() {
		group.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				group.save();
			}
		}).throwsException().withMessage("Reading group requires name.");
	}

	@Test
	public void shouldUpdateIdAndSaveReadingsOnSave() {
		Long id = mock(Long.class);
		Reading reading = mock(Reading.class);
		prepareSave(id, reading);

		assertFalse(group.hasId());
		group.save();
		assertSame(id, group.getId());
		verify(reading).save();
	}

	private void prepareSave(Long id, Reading... readings) {
		group.setReadings(sortedSet(readings));
		when(dao.save(group)).thenReturn(id);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		group.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfHasId() {
		Long id = mock(Long.class);
		Whitebox.setInternalState(group, "id", id);

		assertTrue(group.hasId());
		group.delete();
		assertFalse(group.hasId());
		verify(dao).delete(id);
	}

	@Test
	public void shouldNotifyReadingsOfDeletion() {
		Reading reading = mock(Reading.class);
		group.setReadings(sortedSet(reading));

		group.delete();
		verify(reading).deleted();
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(group.getName(), "" + group);
	}
}