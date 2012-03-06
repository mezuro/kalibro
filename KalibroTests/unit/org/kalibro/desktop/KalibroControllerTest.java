package org.kalibro.desktop;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.settings.SettingsController;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroController.class, SettingsController.class})
public class KalibroControllerTest extends KalibroTestCase {

	private KalibroFrame kalibroFrame;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.mockStatic(SettingsController.class);
		mockKalibroFrame();
	}

	private void mockKalibroFrame() throws Exception {
		kalibroFrame = PowerMockito.mock(KalibroFrame.class);
		PowerMockito.whenNew(KalibroFrame.class).withArguments(any()).thenReturn(kalibroFrame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenSettingsDialogIfSettingsFileDoesNotExist() {
		prepareSettingsFileExists(false);
		KalibroController.main(null);

		PowerMockito.verifyStatic();
		SettingsController.editSettings();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenSettingsDialogIfSettingsFileDoExist() {
		prepareSettingsFileExists(true);
		KalibroController.main(null);

		PowerMockito.verifyStatic(never());
		SettingsController.editSettings();
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