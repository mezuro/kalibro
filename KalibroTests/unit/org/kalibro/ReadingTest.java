package org.kalibro;

import static java.lang.Double.*;
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ReadingTest extends UnitTest {

	private ReadingDao dao;

	private Reading reading;

	@Before
	public void setUp() {
		dao = mock(ReadingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingDao()).thenReturn(dao);
		reading = loadFixture("excellent", Reading.class);
	}

	@Test
	public void shouldSortByGrade() {
		assertSorted(withGrade(NEGATIVE_INFINITY), withGrade(0.0), withGrade(42.0), withGrade(POSITIVE_INFINITY));
	}

	@Test
	public void shouldIdentifyByLabel() {
		assertEquals(reading, withLabel(reading.getLabel()));
	}

	@Test
	public void checkConstruction() {
		reading = new Reading();
		assertFalse(reading.hasId());
		assertEquals("", reading.getLabel());
		assertDoubleEquals(0.0, reading.getGrade());
		assertEquals(Color.WHITE, reading.getColor());
	}

	@Test
	public void shouldNotSetConflictingLabel() {
		groupWith(withLabel("label"));
		reading.setLabel("original");
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				reading.setLabel("label");
			}
		}).throwsException().withMessage("Reading with label \"label\" already exists in the group.");
		assertEquals("original", reading.getLabel());
	}

	@Test
	public void shouldNotSetConflictingGrade() {
		groupWith(withGrade(-1.0));
		reading.setGrade(42.0);
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				reading.setGrade(-1.0);
			}
		}).throwsException().withMessage("Reading with grade -1.0 already exists in the group.");
		assertDoubleEquals(42.0, reading.getGrade());
	}

	private void groupWith(Reading other) {
		ReadingGroup group = new ReadingGroup();
		group.addReading(reading);
		group.addReading(other);
	}

	@Test
	public void shouldAssertNoConflictWithOtherReading() {
		reading.assertNoConflictWith(new Reading());
		reading.assertNoConflictWith(new Reading("", 0.0, reading.getColor()));
	}

	private Reading withLabel(String label) {
		return new Reading(label, 0.0, Color.WHITE);
	}

	private Reading withGrade(Double grade) {
		return new Reading("", grade, Color.WHITE);
	}

	@Test
	public void shouldAssertSaved() {
		Long groupId = mock(Long.class);
		groupWithId(groupId);

		reading.assertSaved();
		verify(dao).save(reading, groupId);

		reading.assertSaved();
		verifyNoMoreInteractions(dao);
	}

	@Test
	public void shouldRequireGroupToSave() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				reading.save();
			}
		}).throwsException().withMessage("Reading is not in any group.");
	}

	@Test
	public void shouldAssertGroupSavedBeforeSave() {
		Long groupId = mock(Long.class);
		ReadingGroup group = groupWithId(groupId);

		reading.save();
		InOrder order = Mockito.inOrder(group, dao);
		order.verify(group).assertSaved();
		order.verify(dao).save(reading, groupId);
	}

	@Test
	public void shouldUpdateIdOnSave() {
		Long id = mock(Long.class);
		Long groupId = mock(Long.class);
		groupWithId(groupId);
		when(dao.save(reading, groupId)).thenReturn(id);

		assertFalse(reading.hasId());
		reading.save();
		assertSame(id, reading.getId());
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		reading.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfSaved() {
		Long id = mock(Long.class);
		Whitebox.setInternalState(reading, "id", id);

		assertTrue(reading.hasId());
		reading.delete();
		assertFalse(reading.hasId());
		verify(dao).delete(id);
	}

	@Test
	public void shouldRemoveFromGroupOnDelete() {
		ReadingGroup group = groupWithId(null);
		reading.delete();
		verify(group).removeReading(reading);
	}

	private ReadingGroup groupWithId(Long id) {
		ReadingGroup group = mock(ReadingGroup.class);
		when(group.getId()).thenReturn(id);
		reading.setGroup(group);
		return group;
	}

	@Test
	public void toStringShouldHaveGradeAndLabel() {
		assertEquals("10.0 - Excellent", "" + reading);
	}
}