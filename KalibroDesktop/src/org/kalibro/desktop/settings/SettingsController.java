package org.kalibro.desktop.settings;

import org.kalibro.Kalibro;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.EditDialogListener;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public final class SettingsController implements EditDialogListener<KalibroSettings> {

	public static void editSettings() {
		EditDialog<KalibroSettings> dialog = new EditDialog<KalibroSettings>("Kalibro Settings");
		KalibroSettingsPanel panel = new KalibroSettingsPanel(dialog);
		panel.set(Kalibro.currentSettings());
		dialog.setField(panel);
		dialog.addListener(new SettingsController(dialog));
		dialog.setName("settingsDialog");
		dialog.setResizable(false);
		dialog.setVisible(true);
	}

	private EditDialog<KalibroSettings> dialog;

	private SettingsController(EditDialog<KalibroSettings> dialog) {
		this.dialog = dialog;
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