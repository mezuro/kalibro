package org.kalibro.core.persistence;

import javax.persistence.TypedQuery;

import org.kalibro.Configuration;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.core.persistence.record.MetricConfigurationSnapshotRecord;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link ConfigurationDao}.
 * 
 * @author Carlos Morais
 */
public class ConfigurationDatabaseDao extends DatabaseDao<Configuration, ConfigurationRecord> implements
	ConfigurationDao {

	ConfigurationDatabaseDao() {
		super(ConfigurationRecord.class);
	}

	@Override
	public Long save(Configuration configuration) {
		return save(new ConfigurationRecord(configuration)).id();
	}

	public Configuration snapshotFor(Long processingId) {
		Configuration configuration = new Configuration();
		TypedQuery<MetricConfigurationSnapshotRecord> query = createQuery(
			"SELECT snapshot FROM MetricConfigurationSnapshot snapshot WHERE snapshot.processing = :processingId",
			MetricConfigurationSnapshotRecord.class);
		query.setParameter("processingId", processingId);
		configuration.setMetricConfigurations(DataTransferObject.toSortedSet(query.getResultList()));
		return configuration;
	}
}