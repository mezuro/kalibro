package org.kalibro.core.persistence;

import java.util.List;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.core.persistence.entities.ConfigurationRecord;

class ConfigurationDatabaseDao extends DatabaseDao<Configuration, ConfigurationRecord> implements ConfigurationDao {

	protected ConfigurationDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, ConfigurationRecord.class);
	}

	@Override
	public void save(Configuration configuration) {
		ConfigurationRecord record = new ConfigurationRecord(configuration);
		record = databaseManager.save(record);
		configuration.setId(record.convert().getId());
	}

	@Override
	public List<String> getConfigurationNames() {
		return getAllNames();
	}

	@Override
	public boolean hasConfiguration(String configurationName) {
		return hasEntity(configurationName);
	}

	@Override
	public Configuration getConfiguration(String configurationName) {
		return getByName(configurationName);
	}

	@Override
	public Configuration getConfigurationFor(String projectName) {
		Project project = new ProjectDatabaseDao(databaseManager).getProject(projectName);
		return getByName(project.getConfigurationName());
	}

	@Override
	public void removeConfiguration(String configurationName) {
		ConfigurationRecord record = new ConfigurationRecord(getByName(configurationName));
		databaseManager.delete(record);
	}
}