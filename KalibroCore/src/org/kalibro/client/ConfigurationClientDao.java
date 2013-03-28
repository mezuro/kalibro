package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.Configuration;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.xml.ConfigurationXml;

/**
 * {@link ConfigurationEndpoint} client implementation of {@link ConfigurationDao}.
 * 
 * @author Carlos Morais
 */
class ConfigurationClientDao extends EndpointClient<ConfigurationEndpoint> implements ConfigurationDao {

	ConfigurationClientDao(String serviceAddress) {
		super(serviceAddress, ConfigurationEndpoint.class);
	}

	@Override
	public boolean exists(Long configurationId) {
		return port.configurationExists(configurationId);
	}

	@Override
	public Configuration get(Long configurationId) {
		return port.getConfiguration(configurationId).convert();
	}

	@Override
	public SortedSet<Configuration> all() {
		return DataTransferObject.toSortedSet(port.allConfigurations());
	}

	@Override
	public Long save(Configuration configuration) {
		return port.saveConfiguration(new ConfigurationXml(configuration));
	}

	@Override
	public void delete(Long configurationId) {
		port.deleteConfiguration(configurationId);
	}
}