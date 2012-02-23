package org.kalibro.service;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kalibro.service.entities.ModuleResultXml;

@WebService
public interface ModuleResultEndpoint {

	@WebMethod
	ModuleResultXml getModuleResult(String projectName, String moduleName, Date date);

	@WebMethod
	List<ModuleResultXml> getResultHistory(String projectName, String moduleName);
}