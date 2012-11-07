package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.MetricConfiguration;
import org.kalibro.core.persistence.record.MetricConfigurationRecord;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link MetricConfigurationDao}.
 * 
 * @author Carlos Morais
 */
class MetricConfigurationDatabaseDao extends DatabaseDao<MetricConfiguration, MetricConfigurationRecord>
	implements MetricConfigurationDao {

	MetricConfigurationDatabaseDao() {
		super(MetricConfigurationRecord.class);
	}

	@Override
	public SortedSet<MetricConfiguration> metricConfigurationsOf(Long configurationId) {
		String where = "metricConfiguration.configuration.id = :configurationId";
		TypedQuery<MetricConfigurationRecord> query = createRecordQuery(where);
		query.setParameter("configurationId", configurationId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(MetricConfiguration metricConfiguration, Long configurationId) {
		return save(new MetricConfigurationRecord(metricConfiguration, configurationId)).id();
	}
}