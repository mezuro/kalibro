package org.kalibro;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ReadingTest extends UnitTest {

	private Reading reading;
	private ReadingDao dao;

	@Before
	public void setUp() {
		dao = mock(ReadingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingDao()).thenReturn(dao);
		reading = loadFixture("excellent", Reading.class);
	}

	@Test
	public void shouldSortByGrade() {
		assertSorted(reading(Double.NEGATIVE_INFINITY), reading(0.0), reading(42.0), reading(Double.POSITIVE_INFINITY));
	}

	@Test
	public void checkDefaultReading() {
		reading = new Reading();
		assertFalse(reading.hasId());
		assertEquals("", reading.getLabel());
		assertDoubleEquals(0.0, reading.getGrade());
		assertEquals(Color.WHITE, reading.getColor());
	}

	@Test
	public void shouldNotSetConflictingLabel() {
		ReadingGroup group = new ReadingGroup();
		group.addReading(reading);
		group.addReading(reading("label"));
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
		ReadingGroup group = new ReadingGroup();
		group.addReading(reading);
		group.addReading(reading(-1.0));
		reading.setGrade(42.0);
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				reading.setGrade(-1.0);
			}
		}).throwsException().withMessage("Reading with grade -1.0 already exists in the group.");
		assertDoubleEquals(42.0, reading.getGrade());
	}

	@Test
	public void shouldAssertNoConflictWithOtherReading() {
		reading.assertNoConflictWith(reading("label"));
		reading.assertNoConflictWith(reading(-1.0));
	}

	private Reading reading(String label) {
		return new Reading(label, 42.0, Color.MAGENTA);
	}

	private Reading reading(Double grade) {
		return new Reading("ReadingTest label", grade, Color.MAGENTA);
	}

	@Test
	public void readingsWithSameColorShouldNotConflict() {
		reading.assertNoConflictWith(new Reading("", 42.0, reading.getColor()));
	}

	@Test
	public void shouldGetGroupId() {
		// required for proper saving
		setReadingGroupWithId(42L);
		assertEquals(42L, reading.getGroupId().longValue());
	}

	@Test
	public void shouldNotSaveIfNotGrouped() {
		assertThat(save()).throwsException().withMessage("Reading is not in any group.");
	}

	@Test
	public void shouldNotSaveIfGroupHasNoId() {
		setReadingGroupWithId(null);
		assertThat(save()).throwsException().withMessage("Group is not saved. Save group instead");
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			protected void perform() {
				reading.save();
			}
		};
	}

	@Test
	public void shouldUpdateIdOnSave() {
		setReadingGroupWithId(28L);
		when(dao.save(reading)).thenReturn(42L);

		assertFalse(reading.hasId());
		reading.save();
		assertEquals(42L, reading.getId().longValue());
	}

	@Test
	public void shouldDeleteIfHasId() {
		assertFalse(reading.hasId());
		reading.delete();
		verify(dao, never()).delete(any(Long.class));

		reading.setId(42L);

		assertTrue(reading.hasId());
		reading.delete();
		verify(dao).delete(42L);
		assertFalse(reading.hasId());
	}

	@Test
	public void shouldRemoveFromGroupOnDelete() {
		ReadingGroup group = setReadingGroupWithId(42L);
		reading.delete();
		verify(group).removeReading(reading);
	}

	private ReadingGroup setReadingGroupWithId(Long id) {
		ReadingGroup group = mock(ReadingGroup.class);
		when(group.hasId()).thenReturn(id != null);
		when(group.getId()).thenReturn(id);
		reading.setGroup(group);
		return group;
	}

	@Test
	public void toStringShouldHaveGradeAndLabel() {
		assertEquals("10.0 - Excellent", "" + reading);
	}
}