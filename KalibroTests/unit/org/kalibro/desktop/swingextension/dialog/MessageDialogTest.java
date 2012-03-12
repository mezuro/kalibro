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
public class MessageDialogTest extends KalibroTestCase {

	private static final String TITLE = "MessageDialogTest title";
	private static final String MESSAGE = "MessageDialogTest message";

	private Component parent;
	private MessageDialog dialog;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(JOptionPane.class);
		parent = PowerMockito.mock(Component.class);
		dialog = new MessageDialog(parent);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowMessage() {
		dialog.show(MESSAGE, TITLE);

		PowerMockito.verifyStatic();
		JOptionPane.showMessageDialog(parent, MESSAGE, TITLE, JOptionPane.PLAIN_MESSAGE);
	}
}