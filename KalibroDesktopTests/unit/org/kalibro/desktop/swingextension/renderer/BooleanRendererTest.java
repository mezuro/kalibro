package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.field.BooleanField;
import org.kalibro.tests.UnitTest;

public class BooleanRendererTest extends UnitTest {

	private BooleanRenderer renderer;

	@Before
	public void setUp() {
		renderer = new BooleanRenderer();
	}

	@Test
	public void shouldRenderBoolean() {
		assertTrue(renderer.canRender(true));
		assertTrue(renderer.canRender(false));
	}

	@Test
	public void shouldNotRenderAnythingButBoolean() {
		assertFalse(renderer.canRender(Color.MAGENTA));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender(null));
		assertFalse(renderer.canRender("42"));
	}

	@Test
	public void shouldRenderPlainBooleanField() {
		assertTrue(render(true).getText().isEmpty());
	}

	@Test
	public void shouldRenderWithWhiteBackground() {
		assertEquals(Color.WHITE, render(true).getBackground());
	}

	@Test
	public void shouldRenderTrueWithSelectedField() {
		assertTrue(render(true).isSelected());
	}

	@Test
	public void shouldRenderFalseWithUnselectedField() {
		assertFalse(render(false).isSelected());
	}

	@Test
	public void shouldBeCentralized() {
		BooleanField component = render(false);
		assertEquals(SwingConstants.CENTER, component.getVerticalAlignment());
		assertEquals(SwingConstants.CENTER, component.getHorizontalAlignment());
	}

	private BooleanField render(boolean value) {
		return renderer.render(value);
	}
}