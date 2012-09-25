package org.kalibro.core.persistence;

import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.persistence.record.MetricConfigurationRecord;
import org.kalibro.core.persistence.record.RangeRecord;
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
		Configuration configuration = getConfiguration(configurationName);
		String metricName = metricConfiguration.getMetric().getName();
		if (configuration.containsMetric(metricName))
			configuration.replaceMetricConfiguration(metricName, metricConfiguration);
		else
			configuration.addMetricConfiguration(metricConfiguration);
		recordManager.evictFromCache(RangeRecord.class);
		configurationDao.save(configuration);
	}

	@Override
	public MetricConfiguration getMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		return configuration.getConfigurationFor(metricName);
	}

	@Override
	public void removeMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		configuration.removeMetric(metricName);
		configurationDao.save(configuration);
	}

	private Configuration getConfiguration(String configurationName) {
		return configurationDao.getConfiguration(configurationName);
	}
}