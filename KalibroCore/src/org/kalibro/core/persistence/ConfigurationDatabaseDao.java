package org.kalibro.core.persistence;

import javax.persistence.TypedQuery;

import org.kalibro.Configuration;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.dao.ConfigurationDao;

/**
 * Database access implementation for {@link ConfigurationDao}.
 * 
 * @author Carlos Morais
 */
public class ConfigurationDatabaseDao extends DatabaseDao<Configuration, ConfigurationRecord> implements
	ConfigurationDao {

	public ConfigurationDatabaseDao(RecordManager recordManager) {
		super(recordManager, ConfigurationRecord.class);
	}

	@Override
	public Configuration configurationOf(Long projectId) {
		TypedQuery<ConfigurationRecord> query = createRecordQuery("JOIN Project project WHERE project.id = :projectId");
		query.setParameter("projectId", projectId);
		return query.getSingleResult().convert();
	}

	@Override
	public Long save(Configuration configuration) {
		return save(new ConfigurationRecord(configuration)).id();
	}
}