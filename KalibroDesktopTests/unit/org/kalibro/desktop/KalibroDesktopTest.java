package org.kalibro.desktop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.tests.UtilityClassTest;
import org.kalibro.tests.VoidAnswer;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({KalibroFrame.class, KalibroSettings.class, SettingsController.class})
public class KalibroDesktopTest extends UtilityClassTest {

	private KalibroFrame kalibroFrame;

	@Before
	public void setUp() {
		kalibroFrame = mock(KalibroFrame.class);
		mockStatic(KalibroFrame.class);
		mockStatic(KalibroSettings.class);
		mockStatic(SettingsController.class);
		when(KalibroFrame.getInstance()).thenReturn(kalibroFrame);
	}

	@Test
	public void shouldOpenFrameIfSettingsAlreadyExist() {
		when(KalibroSettings.exists()).thenReturn(true);
		KalibroDesktop.main(null);

		verifyStatic(never());
		SettingsController.editSettings();
		verify(kalibroFrame).setVisible(true);
	}

	@Test
	public void shouldEditSettingsIfTheyDoNotExist() {
		when(KalibroSettings.exists()).thenReturn(false);
		KalibroDesktop.main(null);

		verifyStatic();
		SettingsController.editSettings();
	}

	@Test
	public void shouldNotOpenFrameIfUserDoesNotCreateSettings() {
		when(KalibroSettings.exists()).thenReturn(false);
		KalibroDesktop.main(null);

		verifyStatic(never());
		KalibroFrame.getInstance();
	}

	@Test
	public void shouldOpenFrameIfUserCreatedSettings() throws Exception {
		when(KalibroSettings.exists()).thenReturn(false);
		doAnswer(userCreatesSettings()).when(SettingsController.class, "editSettings");
		KalibroDesktop.main(null);

		verify(kalibroFrame).setVisible(true);
	}

	private Answer<?> userCreatesSettings() {
		return new VoidAnswer() {

			@Override
			public void answer() throws Throwable {
				when(KalibroSettings.exists()).thenReturn(true);
			}
		};
	}
}