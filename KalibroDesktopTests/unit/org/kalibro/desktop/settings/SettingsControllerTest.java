package org.kalibro.desktop.settings;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroException;
import org.kalibro.KalibroSettings;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroSettings.class, SettingsController.class})
public class SettingsControllerTest extends UnitTest {

	private EditDialog<KalibroSettings> dialog;
	private KalibroSettingsPanel panel;
	private KalibroSettings settings;

	private SettingsController controller;

	@Before
	public void setUp() throws Exception {
		mockComponents();
		mockSettings();
		SettingsController.editSettings();
		captureController();
	}

	private void mockComponents() throws Exception {
		dialog = mock(EditDialog.class);
		panel = mock(KalibroSettingsPanel.class);
		whenNew(KalibroSettingsPanel.class).withNoArguments().thenReturn(panel);
		whenNew(EditDialog.class).withArguments(null, "Kalibro Settings", panel).thenReturn(dialog);
	}

	private void mockSettings() {
		settings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.exists()).thenReturn(true);
		when(KalibroSettings.load()).thenReturn(settings);
	}

	private void captureController() {
		ArgumentCaptor<SettingsController> captor = ArgumentCaptor.forClass(SettingsController.class);
		verify(dialog).addListener(captor.capture());
		controller = captor.getValue();
	}

	@Test
	public void shouldShowDefaultSettingsOnFirstTime() {
		when(KalibroSettings.exists()).thenReturn(false);
		SettingsController.editSettings();
		verify(dialog).edit(new KalibroSettings());
	}

	@Test
	public void shouldShowCurrentSettingsIfExists() {
		verify(dialog).edit(settings);
	}

	@Test
	public void dialogShouldNotBeResizable() {
		verify(dialog).setResizable(false);
	}

	@Test
	public void dialogAdjustDialogSizeOnPanelResize() {
		verify(panel).addComponentListener(controller);
		controller.componentResized(null);
		verify(dialog).adjustSize();
	}

	@Test
	public void shouldConfirmValidSettings() {
		assertTrue(controller.dialogConfirm(settings));
		verify(settings).save();
	}

	@Test
	public void shouldNotConfirmInvalidSettingsAndShowError() throws Exception {
		KalibroException error = new KalibroException("SettingsControllerTest");
		ErrorDialog errorDialog = prepareError(error);
		assertFalse(controller.dialogConfirm(settings));
		verify(errorDialog).show(error);
	}

	private ErrorDialog prepareError(KalibroException error) throws Exception {
		ErrorDialog errorDialog = mock(ErrorDialog.class);
		doThrow(error).when(settings).save();
		whenNew(ErrorDialog.class).withArguments(dialog).thenReturn(errorDialog);
		return errorDialog;
	}
}