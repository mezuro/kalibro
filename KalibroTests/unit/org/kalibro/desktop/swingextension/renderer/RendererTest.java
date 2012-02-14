package org.kalibro.desktop.swingextension.renderer;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.powermock.api.mockito.PowerMockito;

public class RendererTest extends KalibroTestCase {

	private Color color;
	private Component component;

	private Renderer renderer;

	@Before
	public void setUp() {
		color = new Color(new Random(System.currentTimeMillis()).nextInt());
		component = PowerMockito.mock(Component.class);
		PowerMockito.when(component.getBackground()).thenReturn(color);
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
		PowerMockito.when(component.getBackground()).thenReturn(Color.WHITE);
		renderer.changeBackgroundIfSelected(component, true);
		verify(component).setBackground(new JTable().getSelectionBackground());
	}

	private class MyRenderer extends Renderer {

		private MyRenderer() {
			super();
		}
	}
}