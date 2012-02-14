package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class FieldSizeTest extends KalibroTestCase {

	private int height, width, fontSize;
	private JComponent component;
	private FieldSize fieldSize;

	@Before
	public void setUp() {
		Random random = new Random(System.currentTimeMillis());
		width = random.nextInt();
		height = random.nextInt();
		fontSize = random.nextInt();
		component = PowerMockito.mock(JComponent.class);
		PowerMockito.when(component.getPreferredSize()).thenReturn(new Dimension(width, height));
		PowerMockito.when(component.getFont()).thenReturn(new Font("Arial", Font.BOLD, fontSize));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetWidthFromComponent() {
		fieldSize = new FieldSize(component);
		assertEquals(width, fieldSize.width);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetHeightFromFontSize() {
		fieldSize = new FieldSize(component);
		assertEquals(2 * fontSize, fieldSize.height);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetMinimumAndPreferredSize() {
		fieldSize = new FieldSize(component);
		Mockito.verify(component).setMinimumSize(fieldSize);
		Mockito.verify(component).setPreferredSize(fieldSize);
	}
}