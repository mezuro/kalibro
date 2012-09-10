package org.kalibro.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kalibro.core.model.ModuleResult;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.entities.ModuleResultXml;

class ModuleResultClientDao extends EndpointClient<ModuleResultEndpoint> implements ModuleResultDao {

	protected ModuleResultClientDao(String serviceAddress) {
		super(serviceAddress, ModuleResultEndpoint.class);
	}

	@Override
	public ModuleResult getModuleResult(String projectName, String moduleName, Date date) {
		return port.getModuleResult(projectName, moduleName, date).convert();
	}

	@Override
	public List<ModuleResult> getResultHistory(String projectName, String moduleName) {
		List<ModuleResult> history = new ArrayList<ModuleResult>();
		List<ModuleResultXml> historyXml = port.getResultHistory(projectName, moduleName);
		for (ModuleResultXml moduleResultXml : historyXml)
			history.add(moduleResultXml.convert());
		return history;
	}
}