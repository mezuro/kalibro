package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;

public class NumberFieldTest extends KalibroTestCase {

	private NumberField<Byte> field;
	private JFormattedTextField innerField;

	@Before
	public void setUp() {
		field = new ByteField("byte");
		innerField = new ComponentFinder(field).find("byte", JFormattedTextField.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHavePlainFont() {
		assertEquals(Font.PLAIN, innerField.getFont().getStyle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveRightHorizontalAlignment() {
		assertEquals(SwingConstants.RIGHT, innerField.getHorizontalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveNullValueByDefault() {
		assertNull(field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testShowRetrieve() {
		field.show((byte) 42);
		assertEquals(new Byte((byte) 42), field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptValidNumber() {
		changeText("42", true);
		assertEquals(new Byte((byte) 42), field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptChangingBackToNull() {
		changeText("42", true);
		changeText("", false);
		assertEquals(new Byte((byte) 42), field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptInvalidNumber() {
		changeText("quarenta e dois", false);
	}

	private void changeText(String text, boolean shouldAccept) {
		try {
			innerField.setText(text);
			innerField.commitEdit();
			assertTrue(shouldAccept);
		} catch (ParseException exception) {
			assertFalse(shouldAccept);
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void specialNumberButtonShouldNotExistByDefault() {
		assertEquals(1, field.getComponentCount());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void specialNumberButtonShouldExistWhenSpecialNumberSet() {
		field = new ByteField("", Byte.MAX_VALUE);
		assertEquals(3, field.getComponentCount());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void buttonShouldSetSpecialNumber() {
		field = new ByteField("", Byte.MIN_VALUE);
		new ComponentFinder(field).find("", Button.class).doClick();
		assertEquals(Byte.MIN_VALUE, field.get().byteValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetBorderOnInnerField() {
		field.setTextBorder(null);
		assertNull(field.getTextBorder());
		assertNull(innerField.getBorder());
	}
}