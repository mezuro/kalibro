package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ConfigurationDao;
import org.kalibro.service.xml.ConfigurationXml;

/**
 * End point to make {@link ConfigurationDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ConfigurationEndpoint", serviceName = "ConfigurationEndpointService")
public interface ConfigurationEndpoint {

	@WebMethod
	@WebResult(name = "exists")
	boolean configurationExists(@WebParam(name = "configurationId") Long configurationId);

	@WebMethod
	@WebResult(name = "configuration")
	ConfigurationXml getConfiguration(@WebParam(name = "configurationId") Long configurationId);

	@WebMethod
	@WebResult(name = "configuration")
	List<ConfigurationXml> allConfigurations();

	@WebMethod
	@WebResult(name = "configurationId")
	Long saveConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration);

	@WebMethod
	void deleteConfiguration(@WebParam(name = "configurationId") Long configurationId);
}