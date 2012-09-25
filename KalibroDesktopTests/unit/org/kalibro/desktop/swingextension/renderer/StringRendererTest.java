package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.JLabel;

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
	public void shouldRenderString() {
		assertTrue(renderer.canRender("42"));
	}

	@Test
	public void shouldNotRenderAnythingButString() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(Color.MAGENTA));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender(null));
	}

	@Test
	public void shouldRenderOpaqueLabel() {
		JLabel component = render("");
		assertTrue(component.isOpaque());
	}

	@Test
	public void shouldRenderWithWhiteBackground() {
		assertEquals(Color.WHITE, render("").getBackground());
	}

	@Test
	public void shouldSetText() {
		assertEquals("My string", render("My string").getText());
	}

	private JLabel render(String value) {
		return renderer.render(value);
	}
}