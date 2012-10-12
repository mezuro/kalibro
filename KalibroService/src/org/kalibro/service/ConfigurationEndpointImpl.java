package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.DataTransferObject;
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
	public ConfigurationXmlResponse configurationOf(@WebParam(name = "repositoryId") Long repositoryId) {
		return new ConfigurationXmlResponse(dao.configurationOf(repositoryId));
	}

	@Override
	@WebResult(name = "configuration")
	public List<ConfigurationXmlResponse> allConfigurations() {
		return DataTransferObject.createDtos(dao.all(), ConfigurationXmlResponse.class);
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