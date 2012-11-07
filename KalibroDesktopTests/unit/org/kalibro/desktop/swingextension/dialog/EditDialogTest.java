package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Window;

import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Language;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.panel.ConfirmPanel;
import org.kalibro.desktop.swingextension.panel.LanguagePanel;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(EditDialog.class)
public class EditDialogTest extends UnitTest {

	private static final String TITLE = "EditDialogTest title";

	private EditDialogListener<Language> listener;
	private LanguagePanel editPanel;
	private Window owner;

	private EditDialog<Language> editDialog;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		createMocks();
		editDialog = spy(new EditDialog<Language>(owner, TITLE, editPanel));
		editDialog.addListener(listener);
		finder = new ComponentFinder(editDialog);

		doNothing().when(editDialog).dispose();
		confirmPanel().addCancelListener(editDialog);
	}

	private void createMocks() {
		listener = mock(EditDialogListener.class);
		editPanel = new LanguagePanel();
		owner = new JDialog();
	}

	@Test
	public void shouldSetOwnerTitleAndName() {
		assertSame(owner, editDialog.getOwner());
		assertEquals(TITLE, editDialog.getTitle());
		assertEquals(editPanel.getName(), editDialog.getName());
	}

	@Test
	public void shouldCreateComponentsAndBuildPanel() {
		assertSame(editPanel, confirmPanel().getComponent(0));
	}

	private ConfirmPanel<Language> confirmPanel() {
		return (ConfirmPanel<Language>) editDialog.getContentPane();
	}

	@Test
	public void shouldSetAndShowOnEdit() {
		doNothing().when(editDialog).setVisible(true);

		editDialog.edit(Language.C);
		assertEquals(Language.C, editPanel.get());
		verify(editDialog).setVisible(true);
	}

	@Test
	public void shouldCloseOnCancel() {
		button("cancel").doClick();
		verify(editDialog).dispose();
	}

	@Test
	public void shouldCloseOnOkIfAcceptedByListener() {
		when(listener.dialogConfirm(Language.CPP)).thenReturn(true);

		editPanel.set(Language.CPP);
		button("ok").doClick();
		verify(listener).dialogConfirm(Language.CPP);
		verify(editDialog).dispose();
	}

	@Test
	public void shouldNotCloseOnOkIfRejectedByListener() {
		when(listener.dialogConfirm(Language.JAVA)).thenReturn(false);

		editPanel.set(Language.JAVA);
		button("ok").doClick();
		verify(listener).dialogConfirm(Language.JAVA);
		verify(editDialog, never()).dispose();
	}

	@Test
	public void shouldShowErrorThrownByListener() throws Exception {
		RuntimeException error = mock(RuntimeException.class);
		ErrorDialog errorDialog = mock(ErrorDialog.class);
		doThrow(error).when(listener).dialogConfirm(Language.PYTHON);
		whenNew(ErrorDialog.class).withArguments(editDialog).thenReturn(errorDialog);

		editPanel.set(Language.PYTHON);
		button("ok").doClick();
		verify(errorDialog).show(error);
		verify(editDialog, never()).dispose();
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}
}