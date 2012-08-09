package org.kalibro.core.persistence.dao;

import java.util.*;

import org.kalibro.core.model.ModuleResult;

public class ModuleResultDaoFake implements ModuleResultDao {

	private SortedMap<ModuleResultKey, ModuleResult> moduleResults = new TreeMap<ModuleResultKey, ModuleResult>();

	public void save(ModuleResult moduleResult, String projectName) {
		String moduleName = moduleResult.getModule().getName();
		Date date = moduleResult.getDate();
		moduleResults.put(new ModuleResultKey(projectName, moduleName, date), moduleResult);
	}

	@Override
	public ModuleResult getModuleResult(String projectName, String moduleName, Date date) {
		return moduleResults.get(new ModuleResultKey(projectName, moduleName, date));
	}

	@Override
	public List<ModuleResult> getResultHistory(String projectName, String moduleName) {
		ModuleResultKey fromKey = new ModuleResultKey(projectName, moduleName, new Date(0));
		ModuleResultKey toKey = new ModuleResultKey(projectName, moduleName, new Date(Long.MAX_VALUE));
		return new ArrayList<ModuleResult>(moduleResults.subMap(fromKey, toKey).values());
	}
}