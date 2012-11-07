package org.kalibro.desktop.settings;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.kalibro.KalibroException;
import org.kalibro.KalibroSettings;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.EditDialogListener;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public final class SettingsController extends ComponentAdapter implements EditDialogListener<KalibroSettings> {

	public static void editSettings() {
		KalibroSettings settings = KalibroSettings.exists() ? KalibroSettings.load() : new KalibroSettings();
		new SettingsController().edit(settings);
	}

	private EditDialog<KalibroSettings> dialog;

	private SettingsController() {
		KalibroSettingsPanel panel = new KalibroSettingsPanel();
		panel.addComponentListener(this);
		dialog = new EditDialog<KalibroSettings>(null, "Kalibro Settings", panel);
		dialog.setResizable(false);
		dialog.addListener(this);
	}

	private void edit(KalibroSettings settings) {
		dialog.edit(settings);
	}

	@Override
	public void componentResized(ComponentEvent event) {
		if (dialog != null)
			dialog.adjustSize();
	}

	@Override
	public boolean dialogConfirm(KalibroSettings settings) {
		try {
			settings.save();
			return true;
		} catch (KalibroException exception) {
			new ErrorDialog(dialog).show(exception);
			return false;
		}
	}
}