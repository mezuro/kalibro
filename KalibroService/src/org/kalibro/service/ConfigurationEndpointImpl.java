package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.kalibro.service.entities.ConfigurationXml;

@WebService
public class ConfigurationEndpointImpl implements ConfigurationEndpoint {

	private ConfigurationDao dao;

	public ConfigurationEndpointImpl() {
		this(DaoFactory.getConfigurationDao());
	}

	protected ConfigurationEndpointImpl(ConfigurationDao configurationDao) {
		dao = configurationDao;
	}

	@Override
	public void saveConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration) {
		dao.save(configuration.convert());
	}

	@Override
	@WebResult(name = "configurationName")
	public List<String> getConfigurationNames() {
		return dao.getConfigurationNames();
	}

	@Override
	@WebResult(name = "hasConfiguration")
	public boolean hasConfiguration(@WebParam(name = "configurationName") String configurationName) {
		return dao.hasConfiguration(configurationName);
	}

	@Override
	@WebResult(name = "configuration")
	public ConfigurationXml getConfiguration(@WebParam(name = "configurationName") String configurationName) {
		return new ConfigurationXml(dao.getConfiguration(configurationName));
	}

	@Override
	public void removeConfiguration(@WebParam(name = "configurationName") String configurationName) {
		dao.removeConfiguration(configurationName);
	}
}