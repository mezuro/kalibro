package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.text.DecimalFormatSymbols;

import javax.swing.JFormattedTextField;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;

public class DoubleFieldTest extends KalibroTestCase {

	private DoubleField field;
	private JFormattedTextField valueField;

	@Before
	public void setUp() {
		field = new DoubleField("field");
		valueField = new ComponentFinder(field).find("field", JFormattedTextField.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveEightColumns() {
		assertEquals(8, field.getColumns());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveAtLeast2FractionDigits() {
		field.setValue(3.0);
		assertEquals("3" + getDecimalSeparator() + "00", valueField.getText());
		field.setValue(3.1);
		assertEquals("3" + getDecimalSeparator() + "10", valueField.getText());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveAtMost2FractionDigits() {
		field.setValue(3.1415);
		assertEquals("3" + getDecimalSeparator() + "14", valueField.getText());
		field.setValue(2.7182);
		assertEquals("2" + getDecimalSeparator() + "72", valueField.getText());
	}

	private char getDecimalSeparator() {
		return new DecimalFormatSymbols().getDecimalSeparator();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetSpecialNumberWhenButtonIsClicked() {
		field = new DoubleField("field", Double.POSITIVE_INFINITY);
		assertNull(field.getValue());

		new ComponentFinder(field).find("field", Button.class).doClick();
		assertDoubleEquals(Double.POSITIVE_INFINITY, field.getValue());
	}
}