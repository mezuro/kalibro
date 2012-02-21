package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.kalibro.service.entities.ConfigurationXml;

@WebService
public interface ConfigurationEndpoint {

	@WebMethod
	void saveConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration);

	@WebMethod
	List<String> getConfigurationNames();

	@WebMethod
	ConfigurationXml getConfiguration(String configurationName);

	@WebMethod
	void removeConfiguration(String configurationName);
}