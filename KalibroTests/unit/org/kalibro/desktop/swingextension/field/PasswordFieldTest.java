package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class PasswordFieldTest extends KalibroTestCase {

	private PasswordField field;

	@Before
	public void setUp() {
		field = new PasswordField("", 5);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotTrimText() {
		field.set("  my text  ");
		assertEquals("  my text  ", field.get());
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