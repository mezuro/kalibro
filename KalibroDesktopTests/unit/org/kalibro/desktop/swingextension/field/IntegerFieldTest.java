package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import javax.swing.JFormattedTextField;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class IntegerFieldTest extends UnitTest {

	private static final String NAME = "IntegerFieldTest name";

	private JFormattedTextField valueField;
	private IntegerField field;

	@Before
	public void setUp() {
		field = new IntegerField(NAME);
		valueField = new ComponentFinder(field).find(NAME, JFormattedTextField.class);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, field.getName());
	}

	@Test
	public void shouldHaveSixColumns() {
		assertEquals(6, field.getColumns());
	}

	@Test
	public void shouldRoundNonIntegerNumbers() {
		shouldRoundTextTo3(Math.PI);
		shouldRoundTextTo3(Math.E);
	}

	private void shouldRoundTextTo3(Number value) {
		valueField.setValue(value);
		assertEquals(value.intValue(), field.get().intValue());
		assertEquals("3", valueField.getText());
	}
}