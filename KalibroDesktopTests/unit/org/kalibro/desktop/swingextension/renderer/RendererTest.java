package org.kalibro.desktop.swingextension.renderer;

import static org.mockito.Matchers.any;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class RendererTest extends TestCase {

	private Color color;
	private Component component;

	private Renderer renderer;

	@Before
	public void setUp() {
		color = new Color(new Random(System.currentTimeMillis()).nextInt());
		component = mock(Component.class);
		when(component.getBackground()).thenReturn(color);
		renderer = new MyRenderer();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotChangeBackgroundIfNotSelected() {
		renderer.changeBackgroundIfSelected(component, false);
		verify(component, never()).setBackground(any(Color.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChangeBackgroundIfSelected() {
		renderer.changeBackgroundIfSelected(component, true);
		verify(component).setBackground(color.darker());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChangeWhiteBackground() {
		when(component.getBackground()).thenReturn(Color.WHITE);
		renderer.changeBackgroundIfSelected(component, true);
		verify(component).setBackground(new JTable().getSelectionBackground());
	}

	private class MyRenderer extends Renderer {

		protected MyRenderer() {
			super();
		}
	}
}