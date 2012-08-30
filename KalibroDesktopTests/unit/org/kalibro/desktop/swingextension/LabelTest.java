package org.kalibro.desktop.swingextension;

import static org.junit.Assert.*;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class LabelTest extends TestCase {

	private Label label;

	@Before
	public void setUp() {
		label = new Label("My label");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, label.getFont().getStyle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveRightHorizontalAlignment() {
		assertEquals(SwingConstants.RIGHT, label.getHorizontalAlignment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, label.getVerticalAlignment());
	}
}