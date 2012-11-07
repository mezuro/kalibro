package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Language;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JOptionPane.class)
public class ChoiceDialogTest extends UnitTest {

	private static final String TITLE = "ChoiceDialogTest title";
	private static final String MESSAGE = "ChoiceDialogTest message";
	private static final Language[] OPTIONS = Language.values();

	private Component parent;

	private ChoiceDialog<Language> dialog;

	@Before
	public void setUp() {
		mockStatic(JOptionPane.class);
		parent = mock(Component.class);
		dialog = new ChoiceDialog<Language>(parent, TITLE);
	}

	@Test
	public void shouldReturnFalseIfNotConfirmed() throws Exception {
		userWillChoose(null);
		assertFalse(dialog.choose(MESSAGE, OPTIONS));
	}

	@Test
	public void shouldChooseFromArray() throws Exception {
		userWillChoose(Language.C);
		assertTrue(dialog.choose(MESSAGE, OPTIONS));
		assertEquals(Language.C, dialog.getChoice());
	}

	@Test
	public void shouldChooseFromCollection() throws Exception {
		userWillChoose(Language.JAVA);
		assertTrue(dialog.choose(MESSAGE, list(OPTIONS)));
		assertEquals(Language.JAVA, dialog.getChoice());
	}

	private void userWillChoose(Language choice) throws Exception {
		doReturn(choice).when(JOptionPane.class, "showInputDialog",
			parent, MESSAGE, TITLE, JOptionPane.PLAIN_MESSAGE, null, OPTIONS, OPTIONS[0]);
	}
}