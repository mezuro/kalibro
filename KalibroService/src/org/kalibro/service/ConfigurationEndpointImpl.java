package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.ConfigurationXml;

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
	public ConfigurationXml getConfiguration(@WebParam(name = "configurationId") Long configurationId) {
		return new ConfigurationXml(dao.get(configurationId));
	}

	@Override
	@WebResult(name = "configuration")
	public ConfigurationXml configurationOf(@WebParam(name = "repositoryId") Long repositoryId) {
		return new ConfigurationXml(dao.configurationOf(repositoryId));
	}

	@Override
	@WebResult(name = "configuration")
	public List<ConfigurationXml> allConfigurations() {
		return DataTransferObject.createDtos(dao.all(), ConfigurationXml.class);
	}

	@Override
	@WebResult(name = "configurationId")
	public Long saveConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration) {
		return dao.save(configuration.convert());
	}

	@Override
	public void deleteConfiguration(@WebParam(name = "configurationId") Long configurationId) {
		dao.delete(configurationId);
	}
}