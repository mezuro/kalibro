package org.kalibro.desktop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.AnswerAdapter;
import org.kalibro.KalibroSettings;
import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.tests.UtilityClassTest;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroSettings.class, KalibroDesktop.class, SettingsController.class})
public class KalibroDesktopTest extends UtilityClassTest {

	private KalibroFrame kalibroFrame;

	@Before
	public void setUp() throws Exception {
		mockStatic(KalibroSettings.class);
		mockStatic(SettingsController.class);
		kalibroFrame = mock(KalibroFrame.class);
		whenNew(KalibroFrame.class).withNoArguments().thenReturn(kalibroFrame);
	}

	@Override
	protected Class<?> utilityClass() {
		return KalibroDesktop.class;
	}

	@Test
	public void shouldNotOpenFrameIfSettingsFileDoesNotExistAndUserCancelsSettingsEdition() throws Exception {
		prepareScenario(false, false);
		KalibroDesktop.main(null);

		verifyStatic();
		SettingsController.editSettings();
		Mockito.verify(kalibroFrame, never()).setVisible(true);
	}

	@Test
	public void shouldOpenFrameIfSettingsFileDoesNotExistButUserConfirmsSettingsEdition() throws Exception {
		prepareScenario(false, true);
		KalibroDesktop.main(null);

		verifyStatic();
		SettingsController.editSettings();
		Mockito.verify(kalibroFrame).setVisible(true);
	}

	@Test
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

	private final class EditSettingsAnswer extends AnswerAdapter {

		private boolean userConfirms;

		@Override
		public void answer() {
			when(KalibroSettings.exists()).thenReturn(userConfirms);
		}
	}
}