package org.kalibro.desktop.swingextension.table;

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
	public void shouldRenderBooleans() {
		assertTrue(renderer.canRender(true));
		assertTrue(renderer.canRender(false));
	}

	@Test
	public void shouldRenderOnlyBooleans() {
		assertFalse(renderer.canRender(42.0));
		assertFalse(renderer.canRender(null));
		assertFalse(renderer.canRender("String"));
		assertFalse(renderer.canRender(list(true, false)));

		assertFalse(renderer.canRender(new Object()));
	}

	@Test
	public void shouldRenderPlainWhiteBooleanField() {
		assertFalse(renderer.render(false).get());

		BooleanField rendered = renderer.render(true);
		assertTrue(rendered.get());
		assertTrue(rendered.isOpaque());
		assertEquals(Color.WHITE, rendered.getBackground());

		assertTrue(rendered.getText().isEmpty());
		assertEquals(SwingConstants.CENTER, rendered.getVerticalAlignment());
		assertEquals(SwingConstants.CENTER, rendered.getHorizontalAlignment());
	}
}