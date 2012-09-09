package org.kalibro.desktop;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.TestCase;
import org.kalibro.desktop.settings.SettingsController;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroSettings.class, KalibroDesktop.class, SettingsController.class})
public class KalibroDesktopTest extends TestCase {

	@BeforeClass
	public static void emmaCoverage() throws Exception {
		KalibroDesktop.class.getDeclaredConstructor().newInstance();
	}

	private KalibroFrame kalibroFrame;

	@Before
	public void setUp() throws Exception {
		mockStatic(KalibroSettings.class);
		mockStatic(SettingsController.class);
		kalibroFrame = mock(KalibroFrame.class);
		whenNew(KalibroFrame.class).withNoArguments().thenReturn(kalibroFrame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenFrameIfSettingsFileDoesNotExistAndUserCancelsSettingsEdition() throws Exception {
		prepareScenario(false, false);
		KalibroDesktop.main(null);

		verifyStatic();
		SettingsController.editSettings();
		Mockito.verify(kalibroFrame, never()).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenFrameIfSettingsFileDoesNotExistButUserConfirmsSettingsEdition() throws Exception {
		prepareScenario(false, true);
		KalibroDesktop.main(null);

		verifyStatic();
		SettingsController.editSettings();
		Mockito.verify(kalibroFrame).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenFrameWithoutEditSettingsIfFileAlreadyExists() throws Exception {
		prepareScenario(true, false);
		KalibroDesktop.main(null);

		verifyStatic(never());
		SettingsController.editSettings();
		Mockito.verify(kalibroFrame).setVisible(true);
	}

	private void prepareScenario(boolean settingsFileExists, boolean userConfirmSettings) throws Exception {
		EditSettingsAnswer answer = new EditSettingsAnswer();
		answer.userConfirms = userConfirmSettings;
		when(KalibroSettings.exists()).thenReturn(settingsFileExists);
		doAnswer(answer).when(SettingsController.class, "editSettings");
	}

	private final class EditSettingsAnswer implements Answer<Object> {

		private boolean userConfirms;

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			when(KalibroSettings.exists()).thenReturn(userConfirms);
			return null;
		}
	}
}