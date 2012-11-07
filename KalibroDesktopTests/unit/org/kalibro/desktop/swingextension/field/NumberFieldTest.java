package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;
import java.text.ParseException;
import java.util.Random;

import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class NumberFieldTest extends UnitTest {

	private static final Byte VALUE = (byte) new Random().nextInt();
	private static final String NAME = "NumberFieldTest name";

	private JFormattedTextField innerField;

	private NumberField<Byte> field;

	@Before
	public void setUp() {
		field = new ByteField(NAME);
		innerField = new ComponentFinder(field).find(NAME, JFormattedTextField.class);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, field.getName());
	}

	@Test
	public void shouldHavePlainFont() {
		assertEquals(Font.PLAIN, innerField.getFont().getStyle());
	}

	@Test
	public void shouldHaveRightHorizontalAlignment() {
		assertEquals(SwingConstants.RIGHT, innerField.getHorizontalAlignment());
	}

	@Test
	public void shouldHaveFieldSize() {
		assertEquals(new FieldSize(field), field.getSize());
		assertEquals(new FieldSize(field), innerField.getPreferredSize());
	}

	@Test
	public void deafultValueShouldBeNull() {
		assertNull(field.get());
	}

	@Test
	public void shouldGet() {
		innerField.setValue(VALUE);
		assertEquals(VALUE, field.get());
	}

	@Test
	public void shouldSet() {
		field.set(VALUE);
		assertEquals(field.getDecimalFormat().format(VALUE), innerField.getText());
	}

	@Test
	public void shouldAcceptValidNumber() {
		validateTextChange(VALUE.toString(), true);
		assertEquals(VALUE, field.get());
	}

	@Test
	public void shouldNotAcceptChangingBackToNull() {
		validateTextChange(VALUE.toString(), true);
		validateTextChange("", false);
		assertEquals(VALUE, field.get());
	}

	@Test
	public void shouldNotAcceptInvalidNumber() {
		validateTextChange("invalid number", false);
	}

	private void validateTextChange(String text, boolean shouldAccept) {
		try {
			innerField.setText(text);
			innerField.commitEdit();
			assertTrue(shouldAccept);
		} catch (ParseException exception) {
			assertFalse(shouldAccept);
		}
	}

	@Test
	public void specialNumberButtonShouldNotExistByDefault() {
		assertEquals(1, field.getComponentCount());
	}

	@Test
	public void buttonClickShouldSetSpecialNumber() {
		field = new ByteField(NAME, Byte.MAX_VALUE);
		Button button = new ComponentFinder(field).find(NAME, Button.class);

		button.doClick();
		assertEquals(Byte.MAX_VALUE, field.get().byteValue());
	}
}