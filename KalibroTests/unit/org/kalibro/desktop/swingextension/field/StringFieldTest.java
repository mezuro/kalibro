package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class StringFieldTest extends KalibroTestCase {

	private StringField field;

	@Before
	public void setUp() {
		field = new StringField("", 5);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldTrimText() {
		field.setValue("  my text  ");
		assertEquals("my text", field.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHavePlainFont() {
		assertEquals(Font.PLAIN, field.getFont().getStyle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveLeftHorizontalAlignment() {
		assertEquals(SwingConstants.LEFT, field.getHorizontalAlignment());
	}
}