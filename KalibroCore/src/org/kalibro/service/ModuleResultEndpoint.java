package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.ModuleResultDao;
import org.kalibro.service.xml.ModuleResultXml;

/**
 * End point to make {@link ModuleResultDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ModuleResultEndpoint", serviceName = "ModuleResultEndpointService")
public interface ModuleResultEndpoint {

	@WebMethod
	@WebResult(name = "moduleResult")
	ModuleResultXml resultsRootOf(@WebParam(name = "processingId") Long processingId);

	@WebMethod
	@WebResult(name = "moduleResult")
	ModuleResultXml parentOf(@WebParam(name = "moduleResultId") Long moduleResultId);

	@WebMethod
	@WebResult(name = "moduleResult")
	List<ModuleResultXml> childrenOf(@WebParam(name = "moduleResultId") Long moduleResultId);
}