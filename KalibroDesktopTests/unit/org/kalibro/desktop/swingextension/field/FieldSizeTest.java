package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class FieldSizeTest extends TestCase {

	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final int WIDTH = RANDOM.nextInt();
	private static final int HEIGHT = RANDOM.nextInt();
	private static final int FONT_SIZE = RANDOM.nextInt();

	private JComponent component;
	private FieldSize fieldSize;

	@Before
	public void setUp() {
		component = PowerMockito.mock(JComponent.class);
		PowerMockito.when(component.getPreferredSize()).thenReturn(new Dimension(WIDTH, HEIGHT));
		PowerMockito.when(component.getFont()).thenReturn(new Font("Arial", Font.BOLD, FONT_SIZE));
		fieldSize = new FieldSize(component);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetWidthFromComponent() {
		assertEquals(WIDTH, fieldSize.width);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetHeightFromFontSize() {
		assertEquals(2 * FONT_SIZE, fieldSize.height);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetMinimumAndPreferredSize() {
		Mockito.verify(component).setMinimumSize(fieldSize);
		Mockito.verify(component).setPreferredSize(fieldSize);
	}
}