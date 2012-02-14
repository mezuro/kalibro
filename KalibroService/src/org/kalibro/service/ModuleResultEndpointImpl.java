package org.kalibro.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.Kalibro;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.service.entities.ModuleResultXml;

@WebService
public class ModuleResultEndpointImpl implements ModuleResultEndpoint {

	@Override
	@WebResult(name = "moduleResult")
	public ModuleResultXml getModuleResult(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "moduleName") String moduleName,
		@WebParam(name = "date") Date date) {
		ModuleResult moduleResult = Kalibro.getModuleResultDao().getModuleResult(projectName, moduleName, date);
		return new ModuleResultXml(moduleResult);
	}

	@Override
	@WebResult(name = "moduleResult")
	public List<ModuleResultXml> getResultHistory(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "moduleName") String moduleName) {
		List<ModuleResultXml> history = new ArrayList<ModuleResultXml>();
		for (ModuleResult result : Kalibro.getModuleResultDao().getResultHistory(projectName, moduleName))
			history.add(new ModuleResultXml(result));
		return history;
	}
}