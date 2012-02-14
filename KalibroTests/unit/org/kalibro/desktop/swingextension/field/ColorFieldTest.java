package org.kalibro.desktop.swingextension.field;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.awt.Color;

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

	private ColorField field;
	private ColorChooser chooser;

	@Before
	public void setUp() throws Exception {
		chooser = PowerMockito.mock(ColorChooser.class);
		PowerMockito.whenNew(ColorChooser.class).withArguments(any(ColorField.class)).thenReturn(chooser);
		field = new ColorField("");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTitle() {
		assertEquals("Choose color", field.getText());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultColorShouldBeWhite() {
		assertEquals(Color.WHITE, field.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetBackgroundWhenSettingValue() {
		field.setValue(Color.BLUE);
		assertEquals(Color.BLUE, field.getBackground());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetBackgroundWhenGettingValue() {
		field.setBackground(Color.BLUE);
		assertEquals(Color.BLUE, field.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowColorDialogWithCurrentValueSelected() {
		field.setValue(Color.GREEN);
		field.doClick();
		Mockito.verify(chooser).chooseColor(Color.GREEN);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetColorWhenColorDialogCloses() {
		PowerMockito.when(chooser.chooseColor(Color.YELLOW)).thenReturn(Color.RED);
		field.setValue(Color.YELLOW);
		field.doClick();
		assertEquals(Color.RED, field.getValue());
	}
}