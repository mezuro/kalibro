package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.ColorField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;

public class RangePanelTest extends KalibroTestCase {

	private Range range;

	private RangePanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		range = RangeFixtures.amlocRange(RangeLabel.BAD);
		panel = new RangePanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		panel.set(range);
		assertDoubleEquals(range.getBeginning(), doubleField("beginning").get());
		assertDoubleEquals(range.getEnd(), doubleField("end").get());
		assertEquals(range.getLabel(), labelField().get());
		assertDoubleEquals(range.getGrade(), doubleField("grade").get());
		assertEquals(range.getColor(), colorField().get());
		assertEquals(range.getComments(), commentsField().get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		doubleField("beginning").set(range.getBeginning());
		doubleField("end").set(range.getEnd());
		labelField().set(range.getLabel());
		doubleField("grade").set(range.getGrade());
		colorField().set(range.getColor());
		commentsField().set(range.getComments());
		assertDeepEquals(range, panel.get());
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