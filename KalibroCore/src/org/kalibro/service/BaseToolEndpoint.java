package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.BaseToolDao;
import org.kalibro.service.xml.BaseToolXml;

/**
 * End point to make {@link BaseToolDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "BaseToolEndpoint", serviceName = "BaseToolEndpointService")
public interface BaseToolEndpoint {

	@WebMethod
	@WebResult(name = "baseToolName")
	List<String> allBaseToolNames();

	@WebMethod
	@WebResult(name = "baseTool")
	BaseToolXml getBaseTool(@WebParam(name = "baseToolName") String baseToolName);
}