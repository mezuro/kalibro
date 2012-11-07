package org.kalibro.desktop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.tests.UtilityClassTest;
import org.kalibro.tests.VoidAnswer;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroDesktop.class, KalibroSettings.class, SettingsController.class})
public class KalibroDesktopTest extends UtilityClassTest {

	private KalibroFrame kalibroFrame;

	@Before
	public void setUp() throws Exception {
		mockStatic(KalibroSettings.class);
		mockStatic(SettingsController.class);
		kalibroFrame = mock(KalibroFrame.class);
		whenNew(KalibroFrame.class).withNoArguments().thenReturn(kalibroFrame);
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
	public void shouldNotOpenFrameIfUserDoesNotCreateSettings() throws Exception {
		when(KalibroSettings.exists()).thenReturn(false);
		KalibroDesktop.main(null);

		verifyNew(KalibroFrame.class, never()).withNoArguments();
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