package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.AnswerAdapter;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ColorChooser.class, JColorChooser.class})
public class ColorChooserTest extends TestCase {

	private static final Color DEFAULT_COLOR = new Color(new Random(System.currentTimeMillis()).nextInt());
	private static final Color SELECTED_COLOR = DEFAULT_COLOR.brighter();

	private Component parent;
	private JDialog chooserDialog;
	private JColorChooser nativeChooser;

	private ColorChooser colorChooser;

	@Before
	public void setUp() throws Exception {
		parent = mock(Component.class);
		mockNativeChooser();
		colorChooser = new ColorChooser(parent);
		mockChooserDialog();
	}

	private void mockNativeChooser() throws Exception {
		nativeChooser = mock(JColorChooser.class);
		whenNew(JColorChooser.class).withNoArguments().thenReturn(nativeChooser);
	}

	private void mockChooserDialog() {
		chooserDialog = mock(JDialog.class);
		mockStatic(JColorChooser.class);
		when(JColorChooser.createDialog(parent, "Choose color", true, nativeChooser, colorChooser, null)).
			thenReturn(chooserDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetChooserName() {
		Mockito.verify(nativeChooser).setName("colorChooser");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowColorChooserDialogWithDefaultColorSelected() {
		colorChooser.chooseColor(DEFAULT_COLOR);
		Mockito.verify(nativeChooser).setColor(DEFAULT_COLOR);
		Mockito.verify(chooserDialog).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnDefaultColorByDefault() {
		assertSame(DEFAULT_COLOR, colorChooser.chooseColor(DEFAULT_COLOR));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnSelectedColorWhenSelected() {
		doAnswer(selectColor()).when(chooserDialog).setVisible(true);
		assertSame(SELECTED_COLOR, colorChooser.chooseColor(DEFAULT_COLOR));
	}

	private Answer<?> selectColor() {
		return new AnswerAdapter() {

			@Override
			protected void answer() {
				when(nativeChooser.getColor()).thenReturn(SELECTED_COLOR);
				colorChooser.actionPerformed(null);
			}
		};
	}
}