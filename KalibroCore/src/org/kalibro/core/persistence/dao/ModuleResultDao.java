package org.kalibro.core.persistence.dao;

import java.util.Date;
import java.util.List;

import org.kalibro.core.model.ModuleResult;

public interface ModuleResultDao {

	void save(ModuleResult moduleResult, String projectName, Date date);

	ModuleResult getModuleResult(String projectName, String moduleName, Date date);

	List<ModuleResult> getResultHistory(String projectName, String moduleName);
}