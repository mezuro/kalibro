package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class BooleanFieldTest extends TestCase {

	private static final Boolean[] VALUES = new Boolean[]{true, false};

	private BooleanField field;

	@Before
	public void setUp() {
		field = new BooleanField("", "My boolean field");
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
	public void shouldGet() {
		for (Boolean value : VALUES) {
			field.setSelected(value);
			assertEquals(value, field.get());
		}
	}

	@Test
	public void shouldSet() {
		for (Boolean value : VALUES) {
			field.set(value);
			assertEquals(value, field.isSelected());
		}
	}
}