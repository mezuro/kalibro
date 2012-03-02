package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.Language;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JOptionPane.class)
public class ChoiceDialogTest extends KalibroTestCase {

	private static final String TITLE = "ChoiceDialogTest title";
	private static final String MESSAGE = "ChoiceDialogTest message";
	private static final Language[] OPTIONS = Language.values();

	private Component parent;

	private ChoiceDialog<Language> dialog;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(JOptionPane.class);
		parent = PowerMockito.mock(Component.class);
		dialog = new ChoiceDialog<Language>(TITLE, parent);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnFalseIfNotConfirmed() throws Exception {
		mockJOptionPane(null);
		assertFalse(dialog.choose(MESSAGE, OPTIONS));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChooseFromArray() throws Exception {
		mockJOptionPane(Language.C);
		assertTrue(dialog.choose(MESSAGE, OPTIONS));
		assertEquals(Language.C, dialog.getChoice());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChooseFromCollection() throws Exception {
		mockJOptionPane(Language.JAVA);
		assertTrue(dialog.choose(MESSAGE, Arrays.asList(OPTIONS)));
		assertEquals(Language.JAVA, dialog.getChoice());
	}

	private void mockJOptionPane(Language choice) throws Exception {
		PowerMockito.doReturn(choice).when(JOptionPane.class, "showInputDialog",
			parent, MESSAGE, TITLE, JOptionPane.PLAIN_MESSAGE, null, OPTIONS, OPTIONS[0]);
	}
}