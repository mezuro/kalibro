package org.kalibro.desktop;

import org.kalibro.Kalibro;
import org.kalibro.desktop.settings.SettingsDialog;

public class KalibroController implements KalibroFrameListener {

	public static void main(String[] arguments) {
		KalibroController controller = new KalibroController();
		if (! Kalibro.settingsFileExists())
			controller.editSettings();
		if (Kalibro.settingsFileExists())
			controller.showFrame();
	}

	public void showFrame() {
		new KalibroFrame(this).setVisible(true);
	}

	@Override
	public void editSettings() {
		new SettingsDialog().setVisible(true);
	}
}