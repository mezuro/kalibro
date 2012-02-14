package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JOptionPane.class)
public class ErrorDialogTest extends KalibroTestCase {

	private Component parent;
	private ErrorDialog dialog;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(JOptionPane.class);
		parent = PowerMockito.mock(Component.class);
		dialog = new ErrorDialog(parent);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowErrorMessage() {
		Exception error = new Exception("Error message");
		dialog.show(error);

		PowerMockito.verifyStatic();
		JOptionPane.showMessageDialog(parent, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}