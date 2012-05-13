package org.kalibro.desktop.configuration;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.kalibro.core.model.Configuration;
import org.kalibro.desktop.CrudMenu;

public class ConfigurationMenu extends CrudMenu<Configuration> {

	public ConfigurationMenu(JDesktopPane desktopPane) {
		super(desktopPane, "Configuration");
	}

	@Override
	protected void initializeController() {
		controller = new ConfigurationController(desktopPane);
	}

	@Override
	protected boolean isEntityFrame(JInternalFrame frame) {
		return frame instanceof ConfigurationFrame;
	}
}