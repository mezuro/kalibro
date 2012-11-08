package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JOptionPane.class)
public class InputDialogTest extends UnitTest {

	private static final String INPUT = "InputDialogTest input";
	private static final String TITLE = "InputDialogTest title";
	private static final String MESSAGE = "InputDialogTest message";

	private Component parent;
	private InputDialog dialog;

	@Before
	public void setUp() {
		mockStatic(JOptionPane.class);
		parent = mock(Component.class);
		dialog = new InputDialog(parent, TITLE);
	}

	@Test
	public void shouldReturnFalseIfNotConfirmed() throws Exception {
		userTypes(null);
		assertFalse(dialog.userTyped(MESSAGE));
	}

	@Test
	public void shouldReturnFalseOnEmptyInput() throws Exception {
		userTypes("   ");
		assertFalse(dialog.userTyped(MESSAGE));
	}

	@Test
	public void shouldGetUserInput() throws Exception {
		userTypes(INPUT);
		assertTrue(dialog.userTyped(MESSAGE));
		assertEquals(INPUT, dialog.getInput());
	}

	private void userTypes(String input) throws Exception {
		doReturn(input).when(JOptionPane.class, "showInputDialog", parent, MESSAGE, TITLE, JOptionPane.PLAIN_MESSAGE);
	}
}