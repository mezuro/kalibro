package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroException;
import org.kalibro.KalibroSettings;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.Kalibro;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, SettingsController.class})
public class SettingsControllerTest extends KalibroTestCase {

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
		panel = PowerMockito.mock(KalibroSettingsPanel.class);
		PowerMockito.whenNew(KalibroSettingsPanel.class).withNoArguments().thenReturn(panel);
	}

	private void mockDialog() throws Exception {
		dialog = PowerMockito.mock(EditDialog.class);
		PowerMockito.whenNew(EditDialog.class).withArguments("Kalibro Settings", panel).thenReturn(dialog);
	}

	private void mockKalibro() {
		settings = new KalibroSettings();
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
	}

	private void captureController() {
		ArgumentCaptor<SettingsController> captor = ArgumentCaptor.forClass(SettingsController.class);
		Mockito.verify(dialog).addListener(captor.capture());
		controller = captor.getValue();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowCurrentSettings() {
		Mockito.verify(dialog).edit(settings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void dialogShouldNotBeResizable() {
		Mockito.verify(dialog).setResizable(false);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void dialogAdjustDialogSizeOnPanelResize() {
		Mockito.verify(panel).addComponentListener(controller);
		controller.componentResized(null);
		Mockito.verify(dialog).adjustSize();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfirmValidSettings() {
		assertTrue(controller.dialogConfirm(settings));

		PowerMockito.verifyStatic();
		Kalibro.changeSettings(settings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotConfirmInvalidSettingsAndShowError() throws Exception {
		KalibroException error = new KalibroException("SettingsControllerTest");
		ErrorDialog errorDialog = prepareError(error);
		assertFalse(controller.dialogConfirm(settings));
		verify(errorDialog).show(error);
	}

	private ErrorDialog prepareError(KalibroException error) throws Exception {
		ErrorDialog errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.doThrow(error).when(Kalibro.class, "changeSettings", settings);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(dialog).thenReturn(errorDialog);
		return errorDialog;
	}
}