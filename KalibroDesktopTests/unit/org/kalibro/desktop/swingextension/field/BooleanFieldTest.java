package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;

@RunWith(Theories.class)
public class BooleanFieldTest extends UnitTest {

	private static final String NAME = "BooleanFieldTest name";
	private static final String TEXT = "BooleanFieldTest text";

	@DataPoints
	public static final Boolean[] values() {
		return array(true, false);
	}

	private BooleanField field;

	@Before
	public void setUp() {
		field = new BooleanField(NAME, TEXT);
	}

	@Test
	public void shouldSetNameAndText() {
		assertEquals(NAME, field.getName());
		assertEquals(TEXT, field.getText());
	}

	@Test
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, field.getFont().getStyle());
	}

	@Test
	public void shouldHaveCentertHorizontalAlignment() {
		assertEquals(SwingConstants.CENTER, field.getHorizontalAlignment());
	}

	@Test
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, field.getVerticalAlignment());
	}

	@Test
	public void shouldHaveFieldSize() {
		assertEquals(new FieldSize(field), field.getSize());
	}

	@Theory
	public void shouldGet(boolean value) {
		field.setSelected(value);
		assertEquals(value, field.get());
	}

	@Theory
	public void shouldSet(Boolean value) {
		field.set(value);
		assertEquals(value, field.isSelected());
	}
}