package org.kalibro.desktop.swingextension;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.field.FieldSize;
import org.kalibro.tests.UnitTest;

public class LabelTest extends UnitTest {

	private static final String TEXT = "LabelTest text";

	private Label label;

	@Before
	public void setUp() {
		label = new Label(TEXT);
	}

	@Test
	public void shouldSetText() {
		assertEquals(TEXT, label.getText());
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

	@Test
	public void shouldHaveFieldSize() {
		assertEquals(new FieldSize(label), label.getSize());
	}
}