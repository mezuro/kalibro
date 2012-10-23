package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.desktop.swingextension.dialog.ColorChooser;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(ColorField.class)
public class ColorFieldTest extends UnitTest {

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

	@Test
	public void checkTitle() {
		assertEquals("Choose color", field.getText());
	}

	@Test
	public void defaultColorShouldBeWhite() {
		assertEquals(Color.WHITE, field.get());
	}

	@Test
	public void shouldGet() {
		field.setBackground(COLOR);
		assertEquals(COLOR, field.get());
	}

	@Test
	public void shouldSet() {
		field.set(COLOR);
		assertEquals(COLOR, field.getBackground());
	}

	@Test
	public void shouldSetForegroundToContrastWithBackground() {
		field.set(COLOR);
		int expectedRGB = 0xff000000 | (Integer.MAX_VALUE - COLOR.getRGB());
		assertEquals(expectedRGB, field.getForeground().getRGB());
	}

	@Test
	public void shouldShowColorDialogWithCurrentValueSelected() {
		field.set(COLOR);
		field.doClick();
		Mockito.verify(chooser).chooseColor(COLOR);
	}

	@Test
	public void shouldSetColorWhenColorDialogCloses() {
		field.set(COLOR);
		field.doClick();
		assertEquals(COLOR.brighter(), field.get());
	}
}