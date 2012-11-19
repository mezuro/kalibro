package org.kalibro.desktop.configuration;

import static org.kalibro.tests.UnitTest.loadFixture;

import org.kalibro.Configuration;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

@Deprecated
public final class ConfigurationPanelManualTest {

	public static void main(String[] args) {
		new ConfigurationPanelManualTest();
	}

	private ConfigurationPanelManualTest() {
		ConfigurationPanel panel = new ConfigurationPanel();
		panel.set(loadFixture("sc", Configuration.class));
		new ComponentWrapperDialog("ConfigurationPanel", panel).setVisible(true);
	}
}