package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.tests.UnitTest;

public class DoubleRendererTest extends UnitTest {

	private DoubleRenderer renderer;

	@Before
	public void setUp() {
		renderer = new DoubleRenderer();
	}

	@Test
	public void shouldRenderDouble() {
		assertTrue(renderer.canRender(42.0));
	}

	@Test
	public void shouldNotRenderAnythingButDouble() {
		assertFalse(renderer.canRender(true));
		assertFalse(renderer.canRender(Color.MAGENTA));
		assertFalse(renderer.canRender(null));
		assertFalse(renderer.canRender("42"));
	}

	@Test
	public void renderedLabelShouldBeOpaque() {
		assertTrue(render().isOpaque());
	}

	@Test
	public void renderedLabelShouldHaveWhiteBackground() {
		assertEquals(Color.WHITE, render().getBackground());
	}

	@Test
	public void renderedLabelShouldHavePlainFont() {
		assertEquals(Font.PLAIN, render().getFont().getStyle());
	}

	@Test
	public void renderedLabelShouldHaveLeftAlignment() {
		assertEquals(SwingConstants.RIGHT, render().getHorizontalAlignment());
	}

	@Test
	public void shouldSetValue() {
		Double value = 42.0;
		assertEquals(new DoubleField("").getDecimalFormat().format(value), render(value).getText());
	}

	private Label render() {
		return render(42.0);
	}

	private Label render(Double value) {
		return renderer.render(value);
	}
}