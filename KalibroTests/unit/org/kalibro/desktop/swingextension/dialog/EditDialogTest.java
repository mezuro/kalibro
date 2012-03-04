package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.Field;
import org.kalibro.desktop.swingextension.field.PalindromeField;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(EditDialog.class)
public class EditDialogTest extends KalibroTestCase {

	private Field<String> field;
	private ErrorDialog errorDialog;
	private EditDialogListener<String> listener;

	private EditDialog<String> editDialog;
	private ComponentFinder finder;

	@Before
	public void setUp() throws Exception {
		field = new PalindromeField();
		listener = PowerMockito.mock(EditDialogListener.class);

		editDialog = new EditDialog<String>("");
		editDialog.setField(field);
		editDialog.addListener(listener);
		finder = new ComponentFinder(editDialog);
		mockErrorDialog();
	}

	private void mockErrorDialog() throws Exception {
		errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(editDialog).thenReturn(errorDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseOnCancel() {
		assertTrue(editDialog.isDisplayable());
		button("cancel").doClick();
		assertFalse(editDialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowFieldGetErrorAndDontClose() {
		field.set("Not a palindrome");
		button("ok").doClick();
		Mockito.verify(errorDialog).show(any(Exception.class));
		assertTrue(editDialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotCloseIfRejectedByListener() {
		PowerMockito.when(listener.dialogConfirm("4224")).thenReturn(false);
		field.set("4224");
		button("ok").doClick();
		assertTrue(editDialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseIfAcceptedByListener() {
		PowerMockito.when(listener.dialogConfirm("4224")).thenReturn(true);
		field.set("4224");
		button("ok").doClick();
		assertFalse(editDialog.isDisplayable());
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}
}