package org.kalibro.desktop.swingextension.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class RendererTest extends UnitTest {

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

	@Test
	public void shouldNotChangeBackgroundIfNotSelected() {
		renderer.changeBackgroundIfSelected(component, false);
		verify(component, never()).setBackground(any(Color.class));
	}

	@Test
	public void shouldChangeBackgroundIfSelected() {
		renderer.changeBackgroundIfSelected(component, true);
		verify(component).setBackground(color.darker());
	}

	@Test
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