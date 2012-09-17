package org.kalibro.desktop.settings;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroException;
import org.kalibro.KalibroSettings;
import org.kalibro.TestCase;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroSettings.class, SettingsController.class})
public class SettingsControllerTest extends TestCase {

	private EditDialog<KalibroSettings> dialog;
	private KalibroSettingsPanel panel;
	private KalibroSettings settings;

	private SettingsController controller;

	@Before
	public void setUp() throws Exception {
		mockPanel();
		mockDialog();
		mockKalibro();
		SettingsController.editSettings();
		captureController();
	}

	private void mockPanel() throws Exception {
		panel = mock(KalibroSettingsPanel.class);
		whenNew(KalibroSettingsPanel.class).withNoArguments().thenReturn(panel);
	}

	private void mockDialog() throws Exception {
		dialog = mock(EditDialog.class);
		whenNew(EditDialog.class).withArguments("Kalibro Settings", panel).thenReturn(dialog);
	}

	private void mockKalibro() {
		settings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(settings);
	}

	private void captureController() {
		ArgumentCaptor<SettingsController> captor = ArgumentCaptor.forClass(SettingsController.class);
		verify(dialog).addListener(captor.capture());
		controller = captor.getValue();
	}

	@Test
	public void shouldShowCurrentSettings() {
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