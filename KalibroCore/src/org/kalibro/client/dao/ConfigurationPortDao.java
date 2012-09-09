package org.kalibro.client.dao;

import java.util.List;

import org.kalibro.KalibroException;
import org.kalibro.client.EndpointClient;
import org.kalibro.core.dao.ConfigurationDao;
import org.kalibro.core.model.Configuration;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.entities.ConfigurationXml;

class ConfigurationPortDao extends EndpointClient<ConfigurationEndpoint> implements ConfigurationDao {

	protected ConfigurationPortDao(String serviceAddress) {
		super(serviceAddress, ConfigurationEndpoint.class);
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
	public boolean hasConfiguration(String configurationName) {
		return port.hasConfiguration(configurationName);
	}

	@Override
	public Configuration getConfiguration(String configurationName) {
		return port.getConfiguration(configurationName).convert();
	}

	@Override
	public Configuration getConfigurationFor(String projectName) {
		throw new KalibroException("Not available remotely");
	}

	@Override
	public void removeConfiguration(String configurationName) {
		port.removeConfiguration(configurationName);
	}
}