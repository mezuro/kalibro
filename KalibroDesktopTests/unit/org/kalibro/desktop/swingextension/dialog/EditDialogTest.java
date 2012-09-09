package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.PalindromeField;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(EditDialog.class)
public class EditDialogTest extends TestCase {

	private EditDialogListener<String> listener;
	private PalindromeField editPanel;
	private ErrorDialog errorDialog;

	private EditDialog<String> editDialog;
	private ComponentFinder finder;

	@Before
	public void setUp() throws Exception {
		listener = PowerMockito.mock(EditDialogListener.class);
		editPanel = new PalindromeField();

		editDialog = new EditDialog<String>("", editPanel);
		editDialog.addListener(listener);
		finder = new ComponentFinder(editDialog);
		mockErrorDialog();
	}

	private void mockErrorDialog() throws Exception {
		errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(editDialog).thenReturn(errorDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetAndShowOnEdit() {
		Whitebox.setInternalState(AbstractDialog.class, "suppressShow", true);
		editDialog.edit("12321");
		assertEquals("12321", editPanel.get());
		assertFalse(Whitebox.getInternalState(AbstractDialog.class, boolean.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseOnCancel() {
		assertTrue(editDialog.isDisplayable());
		button("cancel").doClick();
		assertFalse(editDialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowErrorFromFieldGetAndDontClose() {
		editPanel.set("Not a palindrome");
		button("ok").doClick();
		Mockito.verify(errorDialog).show(any(Throwable.class));
		assertTrue(editDialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotCloseIfRejectedByListener() {
		PowerMockito.when(listener.dialogConfirm("4224")).thenReturn(false);
		editPanel.set("4224");
		button("ok").doClick();
		assertTrue(editDialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseIfAcceptedByListener() {
		PowerMockito.when(listener.dialogConfirm("4224")).thenReturn(true);
		editPanel.set("4224");
		button("ok").doClick();
		assertFalse(editDialog.isDisplayable());
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}
}