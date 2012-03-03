package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.awt.Color;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.swingextension.dialog.ColorChooser;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(ColorField.class)
public class ColorFieldTest extends KalibroTestCase {

	private static final Color COLOR = new Color(new Random(System.currentTimeMillis()).nextInt());

	private ColorField field;
	private ColorChooser chooser;

	@Before
	public void setUp() throws Exception {
		mockChooser();
		field = new ColorField("");
	}

	private void mockChooser() throws Exception {
		chooser = PowerMockito.mock(ColorChooser.class);
		PowerMockito.whenNew(ColorChooser.class).withArguments(any(ColorField.class)).thenReturn(chooser);
		PowerMockito.when(chooser.chooseColor(COLOR)).thenReturn(COLOR.brighter());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTitle() {
		assertEquals("Choose color", field.getText());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultColorShouldBeWhite() {
		assertEquals(Color.WHITE, field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		field.setBackground(COLOR);
		assertEquals(COLOR, field.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		field.set(COLOR);
		assertEquals(COLOR, field.getBackground());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetForegroundToContrastWithBackground() {
		field.set(COLOR);
		int expectedRGB = 0xff000000 | (Integer.MAX_VALUE - COLOR.getRGB());
		assertEquals(expectedRGB, field.getForeground().getRGB());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowColorDialogWithCurrentValueSelected() {
		field.set(COLOR);
		field.doClick();
		Mockito.verify(chooser).chooseColor(COLOR);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetColorWhenColorDialogCloses() {
		field.set(COLOR);
		field.doClick();
		assertEquals(COLOR.brighter(), field.get());
	}
}