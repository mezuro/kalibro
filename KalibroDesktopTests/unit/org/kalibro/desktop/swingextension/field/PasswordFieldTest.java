package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class PasswordFieldTest extends TestCase {

	private static final String PASSWORD = "  PasswordFieldTest password  ";

	private PasswordField field;

	@Before
	public void setUp() {
		field = new PasswordField("", 5);
		field.set(PASSWORD);
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
	public void shouldNotTrimText() {
		assertEquals(PASSWORD, field.get());
	}
}