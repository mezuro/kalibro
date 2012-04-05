package org.kalibro.core.persistence.database;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Metric;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.persistence.dao.MetricConfigurationDao;
import org.kalibro.core.persistence.database.entities.MetricConfigurationRecord;

class MetricConfigurationDatabaseDao extends DatabaseDao<MetricConfiguration, MetricConfigurationRecord> implements
	MetricConfigurationDao {

	private ConfigurationDatabaseDao configurationDao;

	protected MetricConfigurationDatabaseDao(DatabaseManager databaseManager) {
		super(databaseManager, MetricConfigurationRecord.class);
		configurationDao = new ConfigurationDatabaseDao(databaseManager);
	}

	@Override
	public void save(MetricConfiguration metricConfiguration, String configurationName) {
		Configuration configuration = getConfiguration(configurationName);
		Metric metric = metricConfiguration.getMetric();
		if (configuration.contains(metric))
			configuration.replaceMetricConfiguration(metric, metricConfiguration);
		else
			configuration.addMetricConfiguration(metricConfiguration);
		configurationDao.save(configuration);
	}

	@Override
	public MetricConfiguration getMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		return configuration.getConfigurationFor(new NativeMetric(metricName, null));
	}

	@Override
	public void removeMetricConfiguration(String configurationName, String metricName) {
		Configuration configuration = getConfiguration(configurationName);
		configuration.removeMetric(new NativeMetric(metricName, null));
		configurationDao.save(configuration);
	}

	private Configuration getConfiguration(String configurationName) {
		return configurationDao.getConfiguration(configurationName);
	}
}