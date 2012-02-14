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
		panel.show(range);
		assertDoubleEquals(range.getBeginning(), doubleField("beginning").getValue());
		assertDoubleEquals(range.getEnd(), doubleField("end").getValue());
		assertEquals(range.getLabel(), labelField().getValue());
		assertDoubleEquals(range.getGrade(), doubleField("grade").getValue());
		assertEquals(range.getColor(), colorField().getValue());
		assertEquals(range.getComments(), commentsField().getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		doubleField("beginning").setValue(range.getBeginning());
		doubleField("end").setValue(range.getEnd());
		labelField().setValue(range.getLabel());
		doubleField("grade").setValue(range.getGrade());
		colorField().setValue(range.getColor());
		commentsField().setValue(range.getComments());
		assertDeepEquals(range, panel.retrieve());
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