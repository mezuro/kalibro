package org.kalibro.desktop.swingextension;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class LabelTest extends UnitTest {

	private Label label;

	@Before
	public void setUp() {
		label = new Label("My label");
	}

	@Test
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, label.getFont().getStyle());
	}

	@Test
	public void shouldHaveRightHorizontalAlignment() {
		assertEquals(SwingConstants.RIGHT, label.getHorizontalAlignment());
	}

	@Test
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, label.getVerticalAlignment());
	}
}