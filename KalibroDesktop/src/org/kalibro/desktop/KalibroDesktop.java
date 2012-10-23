package org.kalibro.desktop;

import org.kalibro.KalibroSettings;
import org.kalibro.desktop.settings.SettingsController;

public final class KalibroDesktop {

	public static void main(String[] arguments) {
		if (!KalibroSettings.exists())
			SettingsController.editSettings();
		if (KalibroSettings.exists())
			new KalibroFrame().setVisible(true);
	}

	private KalibroDesktop() {
		return;
	}
}