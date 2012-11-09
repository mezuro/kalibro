package org.kalibro.desktop.configuration;

import org.kalibro.Configuration;
import org.kalibro.desktop.tests.ComponentWrapperDialog;
import org.kalibro.tests.UnitTest;

public final class ConfigurationPanelManualTest extends ConfigurationPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ConfigurationPanel", new ConfigurationPanelManualTest()).setVisible(true);
	}

	private ConfigurationPanelManualTest() {
		super();
		set(UnitTest.loadFixture("sc", Configuration.class));
	}
}