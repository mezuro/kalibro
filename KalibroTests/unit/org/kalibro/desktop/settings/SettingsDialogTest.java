package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;
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
	}

	private void mockKalibro() {
		settings = new KalibroSettings(kalibroSettingsMap());
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotBeResizable() {
		assertFalse(dialog.isResizable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowCurrentSettings() {
		KalibroSettingsPanel settingsPanel = finder.find("settings", KalibroSettingsPanel.class);
		assertDeepEquals(settings, settingsPanel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListenToOkButton() {
		okButton().doClick();
		PowerMockito.verifyStatic();
		Kalibro.changeSettings(settings);
	}

	private Button okButton() {
		return finder.find("ok", Button.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfirmValidSettings() {
		assertTrue(dialog.dialogConfirm(settings));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotConfirmInvalidSettingsAndShowError() throws Exception {
		RuntimeException error = new RuntimeException();
		ErrorDialog errorDialog = prepareError(error);
		assertFalse(dialog.dialogConfirm(settings));
		verify(errorDialog).show(error);
	}

	private ErrorDialog prepareError(RuntimeException error) throws Exception {
		ErrorDialog errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.doThrow(error).when(Kalibro.class, "changeSettings", settings);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(dialog).thenReturn(errorDialog);
		return errorDialog;
	}
}