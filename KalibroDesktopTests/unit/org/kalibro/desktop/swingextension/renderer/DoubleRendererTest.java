package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.tests.UnitTest;

public class DoubleRendererTest extends UnitTest {

	private DoubleRenderer renderer;

	@Before
	public void setUp() {
		renderer = new DoubleRenderer();
	}

	@Test
	public void shouldRenderDoubles() {
		assertTrue(renderer.canRender(42.0));
		assertTrue(renderer.canRender(-0.0));
		assertTrue(renderer.canRender(Double.NaN));
		assertTrue(renderer.canRender(Double.NEGATIVE_INFINITY));
		assertTrue(renderer.canRender(Double.POSITIVE_INFINITY));
	}

	@Test
	public void shouldRenderOnlyDoubles() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(null));
		assertFalse(renderer.canRender("String"));
		assertFalse(renderer.canRender(list(42.0)));

		assertFalse(renderer.canRender(new Object()));
	}

	@Test
	public void shouldRenderOpaqueWhiteLabel() {
		Double value = 42.0;
		JLabel rendered = renderer.render(value);
		assertEquals(format(value), rendered.getText());
		assertTrue(rendered.isOpaque());
		assertEquals(Color.WHITE, rendered.getBackground());

		assertEquals(Font.PLAIN, rendered.getFont().getStyle());
		assertEquals(SwingConstants.RIGHT, rendered.getHorizontalAlignment());
	}

	private String format(Double value) {
		return new DoubleField("").getDecimalFormat().format(value);
	}
}