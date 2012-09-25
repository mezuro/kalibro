package org.kalibro.desktop.configuration;

import static org.junit.Assert.assertEquals;
import static org.kalibro.RangeFixtures.newRange;
import static org.kalibro.RangeLabel.BAD;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.ColorField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.tests.UnitTest;

public class RangePanelTest extends UnitTest {

	private Range range;

	private RangePanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		range = newRange("amloc", BAD);
		panel = new RangePanel();
		finder = new ComponentFinder(panel);
	}

	@Test
	public void shouldGet() {
		doubleField("beginning").set(range.getBeginning());
		doubleField("end").set(range.getEnd());
		labelField().set(range.getLabel());
		doubleField("grade").set(range.getGrade());
		colorField().set(range.getColor());
		commentsField().set(range.getComments());
		assertDeepEquals(range, panel.get());
	}

	@Test
	public void shouldSet() {
		panel.set(range);
		assertDoubleEquals(range.getBeginning(), doubleField("beginning").get());
		assertDoubleEquals(range.getEnd(), doubleField("end").get());
		assertEquals(range.getLabel(), labelField().get());
		assertDoubleEquals(range.getGrade(), doubleField("grade").get());
		assertEquals(range.getColor(), colorField().get());
		assertEquals(range.getComments(), commentsField().get());
	}

	private DoubleField doubleField(String name) {
		return finder.find(name, DoubleField.class);
	}

	private StringField labelField() {
		return finder.find("label", StringField.class);
	}

	private ColorField colorField() {
		return finder.find("color", ColorField.class);
	}

	private TextField commentsField() {
		return finder.find("comments", TextField.class);
	}
}