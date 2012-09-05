package org.kalibro.core.dao;

import java.util.Date;
import java.util.List;

import org.kalibro.core.model.ModuleResult;

public interface ModuleResultDao {

	ModuleResult getModuleResult(String projectName, String moduleName, Date date);

	List<ModuleResult> getResultHistory(String projectName, String moduleName);
}