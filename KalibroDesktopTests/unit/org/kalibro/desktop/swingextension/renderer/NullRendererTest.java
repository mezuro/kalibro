package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NullRendererTest extends UnitTest {

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
	public void shouldRenderOnlyNull() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender("String"));
		assertFalse(renderer.canRender(list((Object) null)));

		assertFalse(renderer.canRender(new Object()));
	}

	@Test
	public void shouldRenderPlainWhitePanel() {
		JPanel rendered = renderer.render(null);
		assertEquals(0, rendered.getComponentCount());
		assertEquals(Color.WHITE, rendered.getBackground());
	}
}