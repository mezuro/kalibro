package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class FieldSizeTest extends UnitTest {

	private Font font;
	private Dimension componentSize;
	private JComponent component;

	private FieldSize fieldSize;

	@Before
	public void setUp() {
		Random random = new Random();
		font = new Font("Arial", Font.BOLD, random.nextInt());
		componentSize = new Dimension(random.nextInt(), random.nextInt());
		component = mock(JComponent.class);
		when(component.getFont()).thenReturn(font);
		when(component.getPreferredSize()).thenReturn(componentSize);
		fieldSize = new FieldSize(component);
	}

	@Test
	public void shouldGetWidthFromComponentSize() {
		assertEquals(componentSize.width, fieldSize.width);
	}

	@Test
	public void shouldGetHeightFromFontSize() {
		assertEquals(2 * font.getSize(), fieldSize.height);
	}

	@Test
	public void shouldSetMinimumAndPreferredSize() {
		verify(component).setMinimumSize(fieldSize);
		verify(component).setPreferredSize(fieldSize);
	}
}