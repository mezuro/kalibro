package org.kalibro.core.persistence.database.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.util.DataTransferObject;

@Entity(name = "Configuration")
public class ConfigurationRecord implements DataTransferObject<Configuration> {

	@Id
	@Column(name = "name", nullable = false)
	private String name;

	@Column
	private String description;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configuration", orphanRemoval = true)
	private Collection<MetricConfigurationRecord> metricConfigurations;

	public ConfigurationRecord() {
		super();
	}

	public ConfigurationRecord(Configuration configuration) {
		name = configuration.getName();
		description = configuration.getDescription();
		initializeMetrics(configuration);
	}

	private void initializeMetrics(Configuration configuration) {
		metricConfigurations = new ArrayList<MetricConfigurationRecord>();
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			metricConfigurations.add(new MetricConfigurationRecord(metricConfiguration, this));
	}

	@Override
	public Configuration convert() {
		Configuration configuration = new Configuration();
		configuration.setName(name);
		configuration.setDescription(description);
		convertMetrics(configuration);
		return configuration;
	}

	private void convertMetrics(Configuration configuration) {
		for (MetricConfigurationRecord metricConfiguration : metricConfigurations)
			configuration.addMetricConfiguration(metricConfiguration.convert());
	}

	protected String getName() {
		return name;
	}
}