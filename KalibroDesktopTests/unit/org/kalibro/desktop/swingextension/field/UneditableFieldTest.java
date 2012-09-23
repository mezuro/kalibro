package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class UneditableFieldTest extends UnitTest {

	private UneditableField<String> field;

	@Before
	public void setUp() {
		field = new UneditableField<String>("");
	}

	@Test
	public void shouldHavePlainFont() {
		assertEquals(Font.PLAIN, field.getFont().getStyle());
	}

	@Test
	public void shouldHaveLeftHorizontalAlignment() {
		assertEquals(SwingConstants.LEFT, field.getHorizontalAlignment());
	}

	@Test
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, field.getVerticalAlignment());
	}

	@Test
	public void shouldShowValue() {
		field.set("My string");
		assertEquals("My string", field.getText());

		field.set(null);
		assertEquals("", field.getText());
	}

	@Test
	public void shouldRetrieveValue() {
		field.set("My string");
		assertEquals("My string", field.get());
	}

	@Test
	public void shouldChangeWidthWhenSettingValue() {
		int emptyWidth = (int) field.getSize().getWidth();

		field.set("123456789");
		int nineWidth = (int) field.getSize().getWidth();
		assertTrue(nineWidth > emptyWidth);

		field.set("1234");
		int fourWidth = (int) field.getSize().getWidth();
		assertTrue(fourWidth < nineWidth);
	}
}