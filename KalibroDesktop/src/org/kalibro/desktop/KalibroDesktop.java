package org.kalibro.desktop;

import org.kalibro.Kalibro;
import org.kalibro.desktop.settings.SettingsController;

public final class KalibroDesktop {

	public static void main(String[] arguments) {
		if (!Kalibro.settingsFileExists())
			SettingsController.editSettings();
		if (Kalibro.settingsFileExists())
			new KalibroFrame().setVisible(true);
	}

	private KalibroDesktop() {
		return;
	}
}