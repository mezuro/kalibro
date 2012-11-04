package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.text.DecimalFormatSymbols;

import javax.swing.JFormattedTextField;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.tests.UnitTest;

public class DoubleFieldTest extends UnitTest {

	private DoubleField field;
	private JFormattedTextField valueField;

	@Before
	public void setUp() {
		field = new DoubleField("field");
		valueField = new ComponentFinder(field).find("field", JFormattedTextField.class);
	}

	@Test
	public void shouldHaveEightColumns() {
		assertEquals(8, field.getColumns());
	}

	@Test
	public void shouldHaveAtLeast2FractionDigits() {
		field.set(3.0);
		assertEquals(format("3.00"), valueField.getText());
		field.set(3.1);
		assertEquals(format("3.10"), valueField.getText());
	}

	@Test
	public void shouldHaveAtMost2FractionDigits() {
		field.set(3.1415);
		assertEquals(format("3.14"), valueField.getText());
		field.set(2.7182);
		assertEquals(format("2.72"), valueField.getText());
	}

	private String format(String numberText) {
		return numberText.replace('.', new DecimalFormatSymbols().getDecimalSeparator());
	}

	@Test
	public void shouldSetSpecialNumberWhenButtonIsClicked() {
		field = new DoubleField("field", Double.POSITIVE_INFINITY);
		assertNull(field.get());

		new ComponentFinder(field).find("field", Button.class).doClick();
		assertDoubleEquals(Double.POSITIVE_INFINITY, field.get());
	}
}