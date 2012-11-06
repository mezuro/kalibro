package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class StringRendererTest extends UnitTest {

	private StringRenderer renderer;

	@Before
	public void setUp() {
		renderer = new StringRenderer();
	}

	@Test
	public void shouldRenderStrings() {
		assertTrue(renderer.canRender(""));
		assertTrue(renderer.canRender("42"));
		assertTrue(renderer.canRender("StringRendererTest"));
	}

	@Test
	public void shouldRenderOnlyStrings() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender(null));
		assertFalse(renderer.canRender(list("String")));

		assertFalse(renderer.canRender(new Object()));
	}

	@Test
	public void shouldRenderOpaqueWhiteLabel() {
		String string = "StringRendererTest string";
		JLabel rendered = renderer.render(string);
		assertEquals(string, rendered.getText());
		assertTrue(rendered.isOpaque());
		assertEquals(Color.WHITE, rendered.getBackground());

		assertEquals(Font.PLAIN, rendered.getFont().getStyle());
		assertEquals(SwingConstants.LEFT, rendered.getHorizontalAlignment());
	}
}