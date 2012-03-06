package org.kalibro.desktop.settings;

import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class KalibroSettingsPanelManualTest extends KalibroSettingsPanel {

	public static void main(String[] args) {
		new KalibroSettingsPanelManualTest(new ComponentWrapperDialog("KalibroSettingsPanel"));
	}

	private KalibroSettingsPanelManualTest(ComponentWrapperDialog dialog) {
		super(dialog);
		set(new KalibroSettings());
		dialog.setComponent(this);
		dialog.setVisible(true);
	}
}