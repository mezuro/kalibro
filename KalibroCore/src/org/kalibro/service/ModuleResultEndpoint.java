package org.kalibro.service;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.entities.ModuleResultXml;

@WebService(name = "ModuleResultEndpoint", serviceName = "ModuleResultEndpointService")
public interface ModuleResultEndpoint {

	@WebMethod
	@WebResult(name = "moduleResult")
	ModuleResultXml getModuleResult(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "moduleName") String moduleName,
		@WebParam(name = "date") Date date);

	@WebMethod
	@WebResult(name = "moduleResult")
	List<ModuleResultXml> getResultHistory(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "moduleName") String moduleName);
}