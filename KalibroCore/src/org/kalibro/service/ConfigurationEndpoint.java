package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.kalibro.service.entities.ConfigurationXml;

@WebService
public interface ConfigurationEndpoint {

	@WebMethod
	public void saveConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration);

	@WebMethod
	public List<String> getConfigurationNames();

	@WebMethod
	public ConfigurationXml getConfiguration(String configurationName);

	@WebMethod
	public void removeConfiguration(String configurationName);
}