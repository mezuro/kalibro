package org.kalibro;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;

public class ReadingTest extends TestCase {

	private Reading reading;

	@Before
	public void setUp() {
		reading = loadFixture("reading-excellent", Reading.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByGrade() {
		assertSorted(reading(-1.0), reading(0.0), reading(42.0), reading(Double.POSITIVE_INFINITY));
	}

	private Reading reading(Double grade) {
		return new Reading("", grade, null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultAttributes() {
		reading = new Reading();
		assertEquals("", reading.getLabel());
		assertDoubleEquals(0.0, reading.getGrade());
		assertEquals(Color.WHITE, reading.getColor());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void readingsWithSameColorShouldNotConflict() {
		reading.assertNoConflictWith(new Reading("", 42.0, reading.getColor()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void readingsWithSameLabelShouldConflict() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				reading.assertNoConflictWith(new Reading(reading.getLabel(), 42.0, Color.WHITE));
			}
		}, "Reading with label '" + reading.getLabel() + "' already exists in the group.");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void readingsWithSameGradeShouldConflict() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				reading.assertNoConflictWith(new Reading("", reading.getGrade(), Color.WHITE));
			}
		}, "Reading with grade '" + reading.getGrade() + "' already exists in the group.");
	}
}