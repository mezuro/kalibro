package org.kalibro.desktop.swingextension;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UtilityClassTest;

public class RendererUtilTest extends UtilityClassTest {

	private static final Color DEFAULT_SELECTION_BACKGROUND = new JTable().getSelectionBackground();

	private Component component;

	@Before
	public void setUp() {
		component = mock(Component.class);
	}

	@Test
	public void shouldNotChangeBackgroundIfNotSelected() {
		RendererUtil.setSelectionBackground(component, false);
		verify(component, never()).setBackground(any(Color.class));
	}

	@Test
	public void shouldChangeWhiteToDefaultSelectionBackground() {
		when(component.getBackground()).thenReturn(Color.WHITE);

		RendererUtil.setSelectionBackground(component, true);
		verify(component).setBackground(DEFAULT_SELECTION_BACKGROUND);
	}

	@Test
	public void shouldDarkenOtherColor() {
		Color color = new Color(new Random().nextInt());
		when(component.getBackground()).thenReturn(color);

		RendererUtil.setSelectionBackground(component, true);
		verify(component).setBackground(color.darker());
	}
}