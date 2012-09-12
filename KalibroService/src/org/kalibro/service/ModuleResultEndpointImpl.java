package org.kalibro.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.core.model.ModuleResult;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.service.entities.ModuleResultXml;

@WebService(name = "ModuleResultEndpoint", serviceName = "ModuleResultEndpointService")
public class ModuleResultEndpointImpl implements ModuleResultEndpoint {

	private ModuleResultDao dao;

	public ModuleResultEndpointImpl() {
		this(DaoFactory.getModuleResultDao());
	}

	protected ModuleResultEndpointImpl(ModuleResultDao moduleResultDao) {
		dao = moduleResultDao;
	}

	@Override
	@WebResult(name = "moduleResult")
	public ModuleResultXml getModuleResult(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "moduleName") String moduleName,
		@WebParam(name = "date") Date date) {
		return new ModuleResultXml(dao.getModuleResult(projectName, moduleName, date));
	}

	@Override
	@WebResult(name = "moduleResult")
	public List<ModuleResultXml> getResultHistory(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "moduleName") String moduleName) {
		List<ModuleResultXml> history = new ArrayList<ModuleResultXml>();
		for (ModuleResult result : dao.getResultHistory(projectName, moduleName))
			history.add(new ModuleResultXml(result));
		return history;
	}
}