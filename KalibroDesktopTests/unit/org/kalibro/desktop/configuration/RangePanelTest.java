package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class RangePanelTest extends UnitTest {

	private Range range;

	private RangePanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		range = loadFixture("lcom4-bad", Range.class);
		Reading bad = range.getReading(), excellent = loadFixture("excellent", Reading.class);
		panel = new RangePanel();
		panel.setPossibleReadings(list(bad, excellent));
		finder = new ComponentFinder(panel);
	}

	@Test
	public void shouldHaveName() {
		assertEquals("range", panel.getName());
	}

	@Test
	public void shouldGet() {
		beginningField().set(range.getBeginning());
		endField().set(range.getEnd());
		readingField().set(range.getReading());
		commentsField().set(range.getComments());
		assertDeepEquals(range, panel.get());
	}

	@Test
	public void shouldSet() {
		panel.set(range);
		assertEquals(range.getBeginning(), beginningField().get());
		assertEquals(range.getEnd(), endField().get());
		assertEquals(range.getReading(), readingField().get());
		assertEquals(range.getComments(), commentsField().get());
	}

	@Test
	public void shouldHaveNullReadingOption() {
		panel.set(new Range());
		assertNull(readingField().get());

		panel.set(range);
		readingField().set(null);
		assertFalse(panel.get().hasReading());
	}

	private DoubleField beginningField() {
		return finder.find("beginning", DoubleField.class);
	}

	private DoubleField endField() {
		return finder.find("end", DoubleField.class);
	}

	private ChoiceField<Reading> readingField() {
		return finder.find("reading", ChoiceField.class);
	}

	private TextField commentsField() {
		return finder.find("comments", TextField.class);
	}
}