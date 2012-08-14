package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class UneditableFieldTest extends KalibroTestCase {

	private UneditableField<String> field;

	@Before
	public void setUp() {
		field = new UneditableField<String>("");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHavePlainFont() {
		assertEquals(Font.PLAIN, field.getFont().getStyle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveLeftHorizontalAlignment() {
		assertEquals(SwingConstants.LEFT, field.getHorizontalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, field.getVerticalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowValue() {
		field.set("My string");
		assertEquals("My string", field.getText());

		field.set(null);
		assertEquals("", field.getText());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveValue() {
		field.set("My string");
		assertEquals("My string", field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
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