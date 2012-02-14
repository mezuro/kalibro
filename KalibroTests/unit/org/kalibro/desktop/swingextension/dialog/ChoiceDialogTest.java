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

	private String title;
	private Component parent;
	private Language[] options;

	private ChoiceDialog<Language> dialog;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(JOptionPane.class);
		title = "Choose Language";
		options = Language.values();
		parent = PowerMockito.mock(Component.class);
		dialog = new ChoiceDialog<Language>(title, parent);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChooseFromCollection() throws Exception {
		mockJOptionPane("Testing ChooseDialog", Language.JAVA);
		assertEquals(Language.JAVA, dialog.choose("Testing ChooseDialog", Arrays.asList(options)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChooseFromArray() throws Exception {
		mockJOptionPane("42", Language.C);
		assertEquals(Language.C, dialog.choose("42", options));
	}

	private void mockJOptionPane(String message, Language choice) throws Exception {
		PowerMockito.doReturn(choice).when(JOptionPane.class, "showInputDialog",
			parent, message, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
	}
}