package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JOptionPane.class)
public class ErrorDialogTest extends TestCase {

	private static final String ERROR_MESSAGE = "ErrorDialogTest";
	private static final Exception ERROR = new Exception(ERROR_MESSAGE);

	private Component parent;
	private ErrorDialog dialog;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(JOptionPane.class);
		parent = PowerMockito.mock(Component.class);
		dialog = new ErrorDialog(parent);
	}

	@Test
	public void shouldShowErrorMessage() {
		dialog.show(ERROR_MESSAGE);
		verifyErrorMessageDialog();
	}

	@Test
	public void shouldShowMessageFromError() {
		dialog.show(ERROR);
		verifyErrorMessageDialog();
	}

	private void verifyErrorMessageDialog() {
		PowerMockito.verifyStatic();
		JOptionPane.showMessageDialog(parent, ERROR_MESSAGE, "Error", JOptionPane.ERROR_MESSAGE);
	}
}