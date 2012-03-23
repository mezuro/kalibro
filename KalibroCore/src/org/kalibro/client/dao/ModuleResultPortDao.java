package org.kalibro.client.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kalibro.KalibroException;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.entities.ModuleResultXml;

class ModuleResultPortDao implements ModuleResultDao {

	private ModuleResultEndpoint port;

	protected ModuleResultPortDao() {
		port = EndpointPortFactory.getEndpointPort(ModuleResultEndpoint.class);
	}

	@Override
	public void save(ModuleResult moduleResult, String projectName) {
		throw new KalibroException("Cannot save module result remotely");
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