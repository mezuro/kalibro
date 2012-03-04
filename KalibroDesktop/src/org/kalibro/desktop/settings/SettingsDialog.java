package org.kalibro.desktop.settings;

import org.kalibro.Kalibro;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.EditDialogListener;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public class SettingsDialog extends EditDialog<KalibroSettings> implements EditDialogListener<KalibroSettings> {

	private KalibroSettingsPanel settingsPanel;

	public SettingsDialog() {
		super("Kalibro Settings");
		setField(settingsPanel);
		addListener(this);
		setResizable(false);
	}

	@Override
	protected void createComponents() {
		super.createComponents();
		settingsPanel = new KalibroSettingsPanel(this);
		settingsPanel.set(Kalibro.currentSettings());
	}

	@Override
	public boolean dialogConfirm(KalibroSettings settings) {
		try {
			Kalibro.changeSettings(settings);
			return true;
		} catch (Exception exception) {
			new ErrorDialog(this).show(exception);
			return false;
		}
	}
}