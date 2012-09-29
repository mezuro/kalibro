package org.kalibro.dao;

import java.util.Date;
import java.util.List;

import org.kalibro.ModuleResult;

public interface ModuleResultDao {

	ModuleResult getModuleResult(String projectName, String moduleName, Date date);

	List<ModuleResult> getResultHistory(String projectName, String moduleName);
}