package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ColorChooser.class, JColorChooser.class})
public class ColorChooserTest extends KalibroTestCase {

	private Color defaultColor, selectedColor;

	private Component parent;
	private JDialog chooserDialog;
	private JColorChooser nativeChooser;

	private ColorChooser colorChooser;

	@Before
	public void setUp() throws Exception {
		parent = mock(Component.class);
		nativeChooser = mock(JColorChooser.class);
		defaultColor = new Color(new Random(System.currentTimeMillis()).nextInt());
		selectedColor = defaultColor.brighter();
		whenNew(JColorChooser.class).withNoArguments().thenReturn(nativeChooser);
		mockChooserDialog();
		colorChooser = new ColorChooser(parent);
	}

	private void mockChooserDialog() {
		chooserDialog = mock(JDialog.class);
		mockStatic(JColorChooser.class);
		when(JColorChooser.createDialog(same(parent), eq("Choose color"), eq(true), same(nativeChooser),
			any(ActionListener.class), any(ActionListener.class))).thenReturn(chooserDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetChooserName() {
		Mockito.verify(nativeChooser).setName("colorChooser");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowColorChooserDialogWithDefaultColorSelected() {
		colorChooser.chooseColor(defaultColor);
		Mockito.verify(nativeChooser).setColor(defaultColor);
		Mockito.verify(chooserDialog).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnDefaultColorWhenCancelled() {
		willSelectDifferentColor();
		assertSame(defaultColor, colorChooser.chooseColor(defaultColor));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnSelectedColorWhenSelected() {
		willSelectDifferentColor();
		willClickOkOnChooserDialog();
		assertSame(selectedColor, colorChooser.chooseColor(defaultColor));
	}

	private void willSelectDifferentColor() {
		PowerMockito.when(nativeChooser.getColor()).thenReturn(selectedColor);
	}

	private void willClickOkOnChooserDialog() {
		doAnswer(new OkAnswer()).when(chooserDialog).setVisible(true);
	}

	private class OkAnswer implements Answer<Object> {

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
			verifyStatic();
			JColorChooser.createDialog(same(parent), eq("Choose color"), eq(true), same(nativeChooser),
				captor.capture(), any(ActionListener.class));
			captor.getValue().actionPerformed(null);
			return null;
		}
	}
}