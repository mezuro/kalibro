package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class BooleanFieldTest extends KalibroTestCase {

	private BooleanField field;

	@Before
	public void setUp() {
		field = new BooleanField("", "My boolean field");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, field.getFont().getStyle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveCentertHorizontalAlignment() {
		assertEquals(SwingConstants.CENTER, field.getHorizontalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, field.getVerticalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowValue() {
		assertFalse(field.isSelected());
		field.setValue(true);
		assertTrue(field.isSelected());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveValue() {
		assertFalse(field.getValue());
		field.setSelected(true);
		assertTrue(field.getValue());
	}
}