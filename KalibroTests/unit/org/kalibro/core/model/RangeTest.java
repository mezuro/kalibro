package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RangeFixtures.newRange;
import static org.kalibro.core.model.RangeLabel.*;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.VoidTask;

public class RangeTest extends TestCase {

	private Range excellent, good, regular, warning, bad;

	@Before
	public void setUp() {
		excellent = newRange("amloc", EXCELLENT);
		good = newRange("amloc", GOOD);
		regular = newRange("amloc", REGULAR);
		warning = newRange("amloc", WARNING);
		bad = newRange("amloc", BAD);
	}

	@Test
	public void checkDefaultValues() {
		Range range = new Range();
		assertDoubleEquals(Double.NEGATIVE_INFINITY, range.getBeginning());
		assertDoubleEquals(Double.POSITIVE_INFINITY, range.getEnd());
		assertEquals("", range.getLabel());
		assertDoubleEquals(0.0, range.getGrade());
		assertEquals(Color.WHITE, range.getColor());
		assertEquals("", range.getComments());
	}

	@Test
	public void testToString() {
		assertEquals("[0.0, 7.0[", "" + excellent);
		assertEquals("[7.0, 10.0[", "" + good);
		assertEquals("[10.0, 13.0[", "" + regular);
		assertEquals("[13.0, 19.5[", "" + warning);
		assertEquals("[19.5, " + Double.POSITIVE_INFINITY + "[", "" + bad);
	}

	@Test
	public void testIsFinite() {
		assertTrue(excellent.isFinite());
		assertTrue(good.isFinite());
		assertTrue(regular.isFinite());
		assertTrue(warning.isFinite());
		assertFalse(bad.isFinite());
	}

	@Test
	public void testContains() {
		assertFalse(excellent.contains(-0.1));
		assertTrue(excellent.contains(0.0));
		assertTrue(excellent.contains(1.0));
		assertFalse(excellent.contains(7.0));
		assertTrue(good.contains(7.0));
	}

	@Test
	public void testIntersectsWith() {
		assertFalse(excellent.intersectsWith(good));
		assertFalse(good.intersectsWith(excellent));

		Range range6to8 = new Range(6.0, 8.0);
		assertTrue(excellent.intersectsWith(range6to8));
		assertTrue(good.intersectsWith(range6to8));
	}

	@Test
	public void shouldEvaluateEqualsByBeginning() {
		assertEquals(excellent, new Range(excellent.getBeginning(), Double.POSITIVE_INFINITY));
		assertEquals(good, new Range(good.getBeginning(), Double.POSITIVE_INFINITY));
		assertEquals(regular, new Range(regular.getBeginning(), Double.POSITIVE_INFINITY));
		assertEquals(warning, new Range(warning.getBeginning(), Double.POSITIVE_INFINITY));
		assertEquals(bad, new Range(bad.getBeginning(), Double.POSITIVE_INFINITY));
	}

	@Test
	public void shouldSortByBeginning() {
		assertSorted(excellent, good, regular, warning, bad);
	}

	@Test
	public void testValidation() {
		assertValid(1.0, 5.0);
		assertValid(Double.NEGATIVE_INFINITY, 0.0);
		assertValid(0.0, Double.POSITIVE_INFINITY);
		assertValid(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		assertInvalid(5.0, 5.0);
		assertInvalid(5.0, 1.0);
		assertInvalid(5.0, 4.99);

		assertInvalid(Double.NaN, 0.0);
		assertInvalid(0.0, Double.NaN);
		assertInvalid(Double.NaN, Double.NaN);

		assertInvalid(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
	}

	private void assertValid(Double beginning, Double end) {
		new Range(beginning, end);
	}

	private void assertInvalid(final Double beginning, final Double end) {
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Exception {
				new Range(beginning, end);
			}
		}).throwsException().withMessage("[" + beginning + ", " + end + "[ is not a valid range");
	}
}