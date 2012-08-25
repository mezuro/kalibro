package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.desktop.swingextension.field.BooleanField;

public class BooleanRendererTest extends TestCase {

	private BooleanRenderer renderer;

	@Before
	public void setUp() {
		renderer = new BooleanRenderer();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderBoolean() {
		assertTrue(renderer.canRender(true));
		assertTrue(renderer.canRender(false));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotRenderAnythingButBoolean() {
		assertFalse(renderer.canRender(Color.MAGENTA));
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender(null));
		assertFalse(renderer.canRender("42"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderPlainBooleanField() {
		assertTrue(render(true).getText().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderWithWhiteBackground() {
		assertEquals(Color.WHITE, render(true).getBackground());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderTrueWithSelectedField() {
		assertTrue(render(true).isSelected());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderFalseWithUnselectedField() {
		assertFalse(render(false).isSelected());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeCentralized() {
		BooleanField component = render(false);
		assertEquals(SwingConstants.CENTER, component.getVerticalAlignment());
		assertEquals(SwingConstants.CENTER, component.getHorizontalAlignment());
	}

	private BooleanField render(boolean value) {
		return renderer.render(value);
	}
}