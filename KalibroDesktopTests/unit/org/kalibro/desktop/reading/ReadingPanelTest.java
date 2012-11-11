package org.kalibro.desktop.reading;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.field.ColorField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class ReadingPanelTest extends UnitTest {

	private Reading reading;

	private ReadingPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		reading = loadFixture("excellent", Reading.class);
		panel = new ReadingPanel();
		finder = new ComponentFinder(panel);
	}

	@Test
	public void shouldHaveName() {
		assertEquals("reading", panel.getName());
	}

	@Test
	public void shouldGet() {
		labelField().set(reading.getLabel());
		gradeField().set(reading.getGrade());
		colorField().set(reading.getColor());
		assertDeepEquals(reading, panel.get());
	}

	@Test
	public void shouldSet() {
		panel.set(reading);
		assertEquals(reading.getLabel(), labelField().get());
		assertEquals(reading.getGrade(), gradeField().get());
		assertEquals(reading.getColor(), colorField().get());
	}

	private StringField labelField() {
		return finder.find("label", StringField.class);
	}

	private DoubleField gradeField() {
		return finder.find("grade", DoubleField.class);
	}

	private ColorField colorField() {
		return finder.find("color", ColorField.class);
	}
}