package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Kalibro;
import org.kalibro.core.model.Configuration;
import org.kalibro.service.entities.ConfigurationXml;

@WebService
public class ConfigurationEndpointImpl implements ConfigurationEndpoint {

	@Override
	public void saveConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration) {
		Kalibro.getConfigurationDao().save(configuration.convert());
	}

	@Override
	@WebResult(name = "configurationName")
	public List<String> getConfigurationNames() {
		return Kalibro.getConfigurationDao().getConfigurationNames();
	}

	@Override
	@WebResult(name = "configuration")
	public ConfigurationXml getConfiguration(@WebParam(name = "configurationName") String configurationName) {
		Configuration configuration = Kalibro.getConfigurationDao().getConfiguration(configurationName);
		return new ConfigurationXml(configuration);
	}

	@Override
	public void removeConfiguration(@WebParam(name = "configurationName") String configurationName) {
		Kalibro.getConfigurationDao().removeConfiguration(configurationName);
	}
}