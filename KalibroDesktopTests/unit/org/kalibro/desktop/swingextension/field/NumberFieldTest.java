package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.tests.UnitTest;

public class NumberFieldTest extends UnitTest {

	private NumberField<Byte> field;
	private JFormattedTextField innerField;

	@Before
	public void setUp() {
		field = new ByteField("byte");
		innerField = new ComponentFinder(field).find("byte", JFormattedTextField.class);
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
	public void deafultValueShouldBeNull() {
		assertNull(field.get());
	}

	@Test
	public void shouldSetAndGet() {
		field.set(new Byte("42"));
		assertEquals(new Byte("42"), field.get());
	}

	@Test
	public void shouldAcceptValidNumber() {
		validateTextChange("42", true);
		assertEquals(new Byte("42"), field.get());
	}

	@Test
	public void shouldNotAcceptChangingBackToNull() {
		validateTextChange("42", true);
		validateTextChange("", false);
		assertEquals(new Byte("42"), field.get());
	}

	@Test
	public void shouldNotAcceptInvalidNumber() {
		validateTextChange("quarenta e dois", false);
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
	public void specialNumberButtonShouldExistWhenSpecialNumberSet() {
		field = new ByteField("", Byte.MAX_VALUE);
		assertEquals(3, field.getComponentCount());
	}

	@Test
	public void buttonShouldSetSpecialNumber() {
		field = new ByteField("", Byte.MIN_VALUE);
		new ComponentFinder(field).find("", Button.class).doClick();
		assertEquals(Byte.MIN_VALUE, field.get().byteValue());
	}

	@Test
	public void shouldSetBorderOnInnerField() {
		field.setTextBorder(null);
		assertNull(field.getTextBorder());
		assertNull(innerField.getBorder());
	}
}