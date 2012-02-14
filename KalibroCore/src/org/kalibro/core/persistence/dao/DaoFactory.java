package org.kalibro.core.persistence.dao;

public interface DaoFactory {

	public BaseToolDao getBaseToolDao();

	public ConfigurationDao getConfigurationDao();

	public ProjectDao getProjectDao();

	public ProjectResultDao getProjectResultDao();

	public ModuleResultDao getModuleResultDao();
}