package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.entities.ConfigurationXml;

@WebService(name = "ConfigurationEndpoint", serviceName = "ConfigurationEndpointService")
public interface ConfigurationEndpoint {

	@WebMethod
	void saveConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration);

	@WebMethod
	@WebResult(name = "configurationName")
	List<String> getConfigurationNames();

	@WebMethod
	@WebResult(name = "hasConfiguration")
	boolean hasConfiguration(@WebParam(name = "configurationName") String configurationName);

	@WebMethod
	@WebResult(name = "configuration")
	ConfigurationXml getConfiguration(@WebParam(name = "configurationName") String configurationName);

	@WebMethod
	void removeConfiguration(@WebParam(name = "configurationName") String configurationName);
}