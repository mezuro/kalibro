package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.Configuration;
import org.kalibro.Granularity;
import org.kalibro.MetricConfiguration;
import org.kalibro.NativeMetric;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.core.persistence.record.MetricConfigurationRecord;
import org.kalibro.dao.MetricConfigurationDao;

class MetricConfigurationDatabaseDao extends DatabaseDao<MetricConfiguration, MetricConfigurationRecord> implements
	MetricConfigurationDao {

	private ConfigurationDatabaseDao configurationDao;

	protected MetricConfigurationDatabaseDao(RecordManager recordManager) {
		super(recordManager, MetricConfigurationRecord.class);
		configurationDao = new ConfigurationDatabaseDao(recordManager);
	}

	@Override
	public void save(MetricConfiguration metricConfiguration, String configurationName) {
		save(new MetricConfigurationRecord(metricConfiguration));
	}

	@Override
	public MetricConfiguration getMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		return configuration.getConfigurationFor(new NativeMetric(metricName, Granularity.CLASS));
	}

	@Override
	public void removeMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		configuration
			.removeMetricConfiguration(new MetricConfiguration(new NativeMetric(metricName, Granularity.CLASS)));
		configurationDao.save(configuration);
	}

	private Configuration getConfiguration(String configurationName) {
		TypedQuery<ConfigurationRecord> query = configurationDao.createRecordQuery("WHERE configuration.name = :name");
		query.setParameter("name", configurationName);
		return query.getSingleResult().convert();
	}

	@Override
	public SortedSet<MetricConfiguration> metricConfigurationsOf(Long configurationId) {
		// TODO Auto-generated method stub
		return null;
	}
}