package org.kalibro.desktop.settings;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.kalibro.Kalibro;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.EditDialogListener;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public final class SettingsController extends ComponentAdapter implements EditDialogListener<KalibroSettings> {

	public static void editSettings() {
		new SettingsController().edit(Kalibro.currentSettings());
	}

	private KalibroSettingsPanel panel;
	private EditDialog<KalibroSettings> dialog;

	private SettingsController() {
		panel = new KalibroSettingsPanel();
		panel.addComponentListener(this);
		dialog = new EditDialog<KalibroSettings>("Kalibro Settings", panel);
		dialog.setName("settingsDialog");
		dialog.setResizable(false);
		dialog.addListener(this);
	}

	private void edit(KalibroSettings settings) {
		panel.set(settings);
		dialog.setVisible(true);
	}

	@Override
	public void componentResized(ComponentEvent event) {
		dialog.adjustSize();
	}

	@Override
	public boolean dialogConfirm(KalibroSettings settings) {
		try {
			Kalibro.changeSettings(settings);
			return true;
		} catch (Exception exception) {
			new ErrorDialog(dialog).show(exception);
			return false;
		}
	}
}