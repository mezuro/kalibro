package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class NullRendererTest extends TestCase {

	private NullRenderer renderer;

	@Before
	public void setUp() {
		renderer = new NullRenderer();
	}

	@Test
	public void shouldRenderNull() {
		assertTrue(renderer.canRender(null));
	}

	@Test
	public void shouldNotRenderAnythingButNull() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(Color.MAGENTA));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender("42"));
	}

	@Test
	public void shouldRenderPlainPanel() {
		assertEquals(0, render().getComponentCount());
	}

	@Test
	public void shouldRenderWithWhiteBackground() {
		assertEquals(Color.WHITE, render().getBackground());
	}

	private JPanel render() {
		return renderer.render(null);
	}
}