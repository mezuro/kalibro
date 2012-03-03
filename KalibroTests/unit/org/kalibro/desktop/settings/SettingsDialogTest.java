package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({Kalibro.class, SettingsDialog.class})
public class SettingsDialogTest extends KalibroTestCase {

	private KalibroSettings settings;

	private SettingsDialog dialog;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		mockKalibro();
		dialog = new SettingsDialog();
		finder = new ComponentFinder(dialog);
		assertTrue(dialog.isDisplayable());
	}

	private void mockKalibro() {
		settings = new KalibroSettings(kalibroSettingsMap());
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowCurrentSettings() {
		KalibroSettingsPanel settingsPanel = finder.find("settings", KalibroSettingsPanel.class);
		assertDeepEquals(settings, settingsPanel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void cancelShouldCloseDialog() {
		button("cancel").doClick();
		assertFalse(dialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void cancelShouldNotChangeSettings() {
		button("cancel").doClick();
		PowerMockito.verifyStatic(never());
		Kalibro.changeSettings(any(KalibroSettings.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void okShouldCloseDialog() {
		button("ok").doClick();
		assertFalse(dialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void okShouldChangeSettings() {
		button("ok").doClick();

		ArgumentCaptor<KalibroSettings> captor = ArgumentCaptor.forClass(KalibroSettings.class);
		PowerMockito.verifyStatic();
		Kalibro.changeSettings(captor.capture());
		assertDeepEquals(settings, captor.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotCloseDialogInCaseOfError() throws Exception {
		mockErrorDialog(new RuntimeException());
		button("ok").doClick();
		assertTrue(dialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowError() throws Exception {
		RuntimeException error = new RuntimeException();
		ErrorDialog errorDialog = mockErrorDialog(error);
		button("ok").doClick();
		verify(errorDialog).show(error);
	}

	private ErrorDialog mockErrorDialog(RuntimeException error) throws Exception {
		ErrorDialog errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.doThrow(error).when(Kalibro.class, "changeSettings", any(KalibroSettings.class));
		PowerMockito.whenNew(ErrorDialog.class).withArguments(dialog).thenReturn(errorDialog);
		return errorDialog;
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}
}