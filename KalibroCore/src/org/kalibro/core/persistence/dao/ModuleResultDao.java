package org.kalibro.core.persistence.dao;

import java.util.Date;
import java.util.List;

import org.kalibro.core.model.ModuleResult;

public interface ModuleResultDao {

	public void save(ModuleResult moduleResult, String projectName, Date date);

	public ModuleResult getModuleResult(String projectName, String moduleName, Date date);

	public List<ModuleResult> getResultHistory(String projectName, String moduleName);
}