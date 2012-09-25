package org.kalibro.core.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import org.kalibro.Configuration;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.dao.ConfigurationDao;

/**
 * Database access implementation for {@link ConfigurationDao}.
 * 
 * @author Carlos Morais
 */
class ConfigurationDatabaseDao extends DatabaseDao<Configuration, ConfigurationRecord> implements ConfigurationDao {

	ConfigurationDatabaseDao(RecordManager recordManager) {
		super(recordManager, ConfigurationRecord.class);
	}

	@Override
	public boolean exists(Long configurationId) {
		return existsWithId(configurationId);
	}

	@Override
	public Configuration get(Long configurationId) {
		return getById(configurationId);
	}

	@Override
	public Configuration configurationOf(Long projectId) {
		TypedQuery<ConfigurationRecord> query = createRecordQuery("JOIN Project project WHERE project.id = :projectId");
		query.setParameter("projectId", projectId);
		return query.getSingleResult().convert();
	}

	@Override
	public List<Configuration> all() {
		return allOrderedByName();
	}

	@Override
	public Long save(Configuration configuration) {
		ConfigurationRecord record = new ConfigurationRecord(configuration);
		return save(record).id();
	}

	@Override
	public void delete(Long configurationId) {
		deleteById(configurationId);
	}
}