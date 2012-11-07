package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormatSymbols;

import javax.swing.JFormattedTextField;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class DoubleFieldTest extends UnitTest {

	private static final String NAME = "DoubleFieldTest name";

	private JFormattedTextField valueField;
	private DoubleField field;

	@Before
	public void setUp() {
		field = new DoubleField(NAME);
		valueField = new ComponentFinder(field).find(NAME, JFormattedTextField.class);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, field.getName());
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
		field.set(Math.PI);
		assertDoubleEquals(Math.PI, field.get());
		assertEquals(format("3.14"), valueField.getText());

		field.set(Math.E);
		assertDoubleEquals(Math.E, field.get());
		assertEquals(format("2.72"), valueField.getText());
	}

	private String format(String numberText) {
		return numberText.replace('.', new DecimalFormatSymbols().getDecimalSeparator());
	}

	@Test
	public void shouldConstructWithSpecialNumber() {
		field = new DoubleField(NAME, Double.POSITIVE_INFINITY);
		Button button = new ComponentFinder(field).find(NAME, Button.class);

		button.doClick();
		assertDoubleEquals(Double.POSITIVE_INFINITY, field.get());
	}
}