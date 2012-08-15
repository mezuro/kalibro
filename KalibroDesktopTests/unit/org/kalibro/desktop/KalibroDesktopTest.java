package org.kalibro.desktop;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.settings.SettingsController;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, KalibroDesktop.class, SettingsController.class})
public class KalibroDesktopTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() throws Exception {
		KalibroDesktop.class.getDeclaredConstructor().newInstance();
	}

	private KalibroFrame kalibroFrame;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.mockStatic(SettingsController.class);
		kalibroFrame = PowerMockito.mock(KalibroFrame.class);
		PowerMockito.whenNew(KalibroFrame.class).withNoArguments().thenReturn(kalibroFrame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenFrameIfSettingsFileDoesNotExistAndUserCancelsSettingsEdition() throws Exception {
		prepareScenario(false, false);
		KalibroDesktop.main(null);

		PowerMockito.verifyStatic();
		SettingsController.editSettings();
		verify(kalibroFrame, never()).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenFrameIfSettingsFileDoesNotExistButUserConfirmsSettingsEdition() throws Exception {
		prepareScenario(false, true);
		KalibroDesktop.main(null);

		PowerMockito.verifyStatic();
		SettingsController.editSettings();
		verify(kalibroFrame).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenFrameWithoutEditSettingsIfFileAlreadyExists() throws Exception {
		prepareScenario(true, false);
		KalibroDesktop.main(null);

		PowerMockito.verifyStatic(never());
		SettingsController.editSettings();
		verify(kalibroFrame).setVisible(true);
	}

	private void prepareScenario(boolean settingsFileExists, boolean userConfirmSettings) throws Exception {
		EditSettingsAnswer answer = new EditSettingsAnswer();
		answer.userConfirms = userConfirmSettings;
		PowerMockito.when(Kalibro.settingsFileExists()).thenReturn(settingsFileExists);
		PowerMockito.doAnswer(answer).when(SettingsController.class, "editSettings");
	}

	private final class EditSettingsAnswer implements Answer<Object> {

		private boolean userConfirms;

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			PowerMockito.when(Kalibro.settingsFileExists()).thenReturn(userConfirms);
			return null;
		}
	}
}