package org.kalibro.client.dao;

import java.util.List;

import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.entities.ConfigurationXml;

public class ConfigurationPortDao implements ConfigurationDao {

	private ConfigurationEndpoint port;

	public ConfigurationPortDao() {
		port = EndpointPortFactory.getEndpointPort(ConfigurationEndpoint.class);
	}

	@Override
	public void save(Configuration configuration) {
		port.saveConfiguration(new ConfigurationXml(configuration));
	}

	@Override
	public List<String> getConfigurationNames() {
		return port.getConfigurationNames();
	}

	@Override
	public Configuration getConfiguration(String configurationName) {
		return port.getConfiguration(configurationName).convert();
	}

	@Override
	public Configuration getConfigurationFor(String projectName) {
		throw new UnsupportedOperationException("Not available remotely");
	}

	@Override
	public void removeConfiguration(String configurationName) {
		port.removeConfiguration(configurationName);
	}
}