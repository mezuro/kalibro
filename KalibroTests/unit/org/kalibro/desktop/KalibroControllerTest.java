package org.kalibro.desktop;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.settings.SettingsDialog;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroController.class})
public class KalibroControllerTest extends KalibroTestCase {

	private KalibroFrame kalibroFrame;
	private SettingsDialog settingsDialog;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Kalibro.class);
		mockKalibroFrame();
		mockSettingsDialog();
	}

	private void mockKalibroFrame() throws Exception {
		kalibroFrame = PowerMockito.mock(KalibroFrame.class);
		PowerMockito.whenNew(KalibroFrame.class).withArguments(any()).thenReturn(kalibroFrame);
	}

	private void mockSettingsDialog() throws Exception {
		settingsDialog = PowerMockito.mock(SettingsDialog.class);
		PowerMockito.whenNew(SettingsDialog.class).withNoArguments().thenReturn(settingsDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenSettingsDialogIfSettingsFileDoesNotExist() {
		prepareSettingsFileExists(false);
		KalibroController.main(null);
		verify(settingsDialog).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenSettingsDialogIfSettingsFileDoExist() {
		prepareSettingsFileExists(true);
		KalibroController.main(null);
		verify(settingsDialog, never()).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenFrameIfSettingsFileDoExist() {
		prepareSettingsFileExists(true);
		KalibroController.main(null);
		verify(kalibroFrame).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenFrameIfSettingsFileDoesNotExist() {
		prepareSettingsFileExists(false);
		KalibroController.main(null);
		verify(kalibroFrame, never()).setVisible(true);
	}

	private void prepareSettingsFileExists(boolean exists) {
		PowerMockito.when(Kalibro.settingsFileExists()).thenReturn(exists);
	}
}