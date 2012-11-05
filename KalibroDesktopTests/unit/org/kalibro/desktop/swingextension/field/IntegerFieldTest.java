package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import javax.swing.JFormattedTextField;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class IntegerFieldTest extends UnitTest {

	private IntegerField field;

	@Before
	public void setUp() {
		field = new IntegerField("field");
	}

	@Test
	public void shouldHaveSixColumns() {
		assertEquals(6, field.getColumns());
	}

	@Test
	public void shouldNotShowFractionDigits() {
		JFormattedTextField valueField = new ComponentFinder(field).find("field", JFormattedTextField.class);
		valueField.setValue(3.1415);
		assertEquals("3", valueField.getText());
		valueField.setValue(2.7182);
		assertEquals("3", valueField.getText());
	}

	@Test
	public void shouldSetSpecialNumberWhenButtonIsClicked() {
		field = new IntegerField("field", Integer.MIN_VALUE);
		assertNull(field.get());

		new ComponentFinder(field).find("field", Button.class).doClick();
		assertEquals(Integer.MIN_VALUE, field.get().intValue());
	}
}