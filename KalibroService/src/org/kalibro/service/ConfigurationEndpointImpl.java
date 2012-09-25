package org.kalibro.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Configuration;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.service.xml.ConfigurationXmlRequest;
import org.kalibro.service.xml.ConfigurationXmlResponse;

/**
 * Implementation of {@link ConfigurationEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ConfigurationEndpoint", serviceName = "ConfigurationEndpointService")
public class ConfigurationEndpointImpl implements ConfigurationEndpoint {

	private ConfigurationDao dao;

	public ConfigurationEndpointImpl() {
		this(DaoFactory.getConfigurationDao());
	}

	public ConfigurationEndpointImpl(ConfigurationDao configurationDao) {
		dao = configurationDao;
	}

	@Override
	@WebResult(name = "exists")
	public boolean configurationExists(@WebParam(name = "configurationId") Long configurationId) {
		return dao.exists(configurationId);
	}

	@Override
	@WebResult(name = "configuration")
	public ConfigurationXmlResponse getConfiguration(@WebParam(name = "configurationId") Long configurationId) {
		return new ConfigurationXmlResponse(dao.get(configurationId));
	}

	@Override
	@WebResult(name = "configuration")
	public ConfigurationXmlResponse configurationOf(@WebParam(name = "projectId") Long projectId) {
		return new ConfigurationXmlResponse(dao.configurationOf(projectId));
	}

	@Override
	@WebResult(name = "configuration")
	public List<ConfigurationXmlResponse> allConfigurations() {
		List<ConfigurationXmlResponse> configurations = new ArrayList<ConfigurationXmlResponse>();
		for (Configuration configuration : dao.all())
			configurations.add(new ConfigurationXmlResponse(configuration));
		return configurations;
	}

	@Override
	@WebResult(name = "configurationId")
	public Long saveConfiguration(@WebParam(name = "configuration") ConfigurationXmlRequest configuration) {
		return dao.save(configuration.convert());
	}

	@Override
	public void deleteConfiguration(@WebParam(name = "configurationId") Long configurationId) {
		dao.delete(configurationId);
	}
}