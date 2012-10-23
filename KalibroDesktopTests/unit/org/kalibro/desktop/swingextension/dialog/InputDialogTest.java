package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JOptionPane.class)
public class InputDialogTest extends UnitTest {

	private static final String TITLE = "InputDialogTest title";
	private static final String MESSAGE = "InputDialogTest message";

	private Component parent;
	private InputDialog dialog;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(JOptionPane.class);
		parent = PowerMockito.mock(Component.class);
		dialog = new InputDialog(TITLE, parent);
	}

	@Test
	public void shouldReturnFalseIfNotConfirmed() throws Exception {
		mockJOptionPane(null);
		assertFalse(dialog.userTyped(MESSAGE));
	}

	@Test
	public void shouldReturnFalseOnEmptyInput() throws Exception {
		mockJOptionPane("   ");
		assertFalse(dialog.userTyped(MESSAGE));
	}

	@Test
	public void shouldGetUserInput() throws Exception {
		mockJOptionPane("the input");
		assertTrue(dialog.userTyped(MESSAGE));
		assertEquals("the input", dialog.getInput());
	}

	private void mockJOptionPane(String input) throws Exception {
		PowerMockito.doReturn(input).when(JOptionPane.class, "showInputDialog",
			parent, MESSAGE, TITLE, JOptionPane.PLAIN_MESSAGE);
	}
}