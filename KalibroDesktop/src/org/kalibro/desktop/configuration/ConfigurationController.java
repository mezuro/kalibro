package org.kalibro.desktop.configuration;

import java.util.List;

import javax.swing.JDesktopPane;

import org.kalibro.core.Kalibro;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.desktop.CrudController;

public class ConfigurationController extends CrudController<Configuration> {

	public ConfigurationController(JDesktopPane desktopPane) {
		super(desktopPane, "Configuration");
	}

	@Override
	protected Configuration createEntity(String name) {
		Configuration configuration = new Configuration();
		configuration.setName(name);
		return configuration;
	}

	@Override
	protected List<String> getEntityNames() {
		return configurationDao().getConfigurationNames();
	}

	@Override
	protected Configuration getEntity(String name) {
		return configurationDao().getConfiguration(name);
	}

	@Override
	protected ConfigurationFrame createFrameFor(Configuration configuration) {
		return new ConfigurationFrame(configuration);
	}

	@Override
	protected void removeEntity(String name) {
		configurationDao().removeConfiguration(name);
	}

	@Override
	protected void save(Configuration configuration) {
		configurationDao().save(configuration);
	}

	@Override
	protected void setEntityName(Configuration configuration, String name) {
		configuration.setName(name);
	}

	private ConfigurationDao configurationDao() {
		return Kalibro.getConfigurationDao();
	}
}