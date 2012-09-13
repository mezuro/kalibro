package org.kalibro;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.Task;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ReadingDao;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ReadingTest extends TestCase {

	private Reading reading;
	private ReadingDao dao;

	@Before
	public void setUp() {
		dao = mock(ReadingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getReadingDao()).thenReturn(dao);
		reading = loadFixture("reading-excellent", Reading.class);
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
	public void readingsWithSameLabelShouldConflict() {
		String label = reading.getLabel();
		shouldConflictWith(reading(label), "Reading with label \"" + label + "\" already exists in the group.");
	}

	@Test
	public void readingsWithSameGradeShouldConflict() {
		Double grade = reading.getGrade();
		shouldConflictWith(reading(grade), "Reading with grade " + grade + " already exists in the group.");
	}

	private Reading reading(String label) {
		return new Reading(label, 42.0, Color.MAGENTA);
	}

	private Reading reading(Double grade) {
		return new Reading("ReadingTest label", grade, Color.MAGENTA);
	}

	private void shouldConflictWith(final Reading other, String message) {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				reading.assertNoConflictWith(other);
			}
		}, message);
	}

	@Test
	public void readingsWithSameColorShouldNotConflict() {
		reading.assertNoConflictWith(new Reading("", 42.0, reading.getColor()));
	}

	@Test
	public void shouldGetGroupId() {
		// required for proper saving
		setReadingGroup(42L);
		assertEquals(42L, reading.getGroupId().longValue());
	}

	@Test
	public void shouldNotSaveIfNotGrouped() {
		checkKalibroExceptionOnSave("Reading is not in any group.");
	}

	@Test
	public void shouldNotSaveIfGroupHasNoId() {
		setReadingGroup(null);
		checkKalibroExceptionOnSave("Group is not saved. Save group instead");
	}

	private void checkKalibroExceptionOnSave(String message) {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				reading.save();
			}
		}, message);
	}

	@Test
	public void shouldUpdateIdOnSave() {
		setReadingGroup(28L);
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
		ReadingGroup group = setReadingGroup(42L);
		reading.delete();
		verify(group).removeReading(reading);
	}

	private ReadingGroup setReadingGroup(Long id) {
		ReadingGroup group = mock(ReadingGroup.class);
		when(group.hasId()).thenReturn(id != null);
		when(group.getId()).thenReturn(id);
		reading.setGroup(group);
		return group;
	}
}