package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.dto.ConfigurationDto;

/**
 * Java Persistence API entity for {@link Configuration}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Configuration")
@Table(name = "\"CONFIGURATION\"")
public class ConfigurationRecord extends ConfigurationDto {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"name\"", nullable = false, unique = true)
	private String name;

	@Column(name = "\"description\"")
	private String description;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "configuration", orphanRemoval = true)
	private Collection<MetricConfigurationRecord> metricConfigurations;

	public ConfigurationRecord() {
		super();
	}

	public ConfigurationRecord(Long id) {
		this.id = id;
	}

	public ConfigurationRecord(Configuration configuration) {
		this(configuration.getId());
		name = configuration.getName();
		description = configuration.getDescription();
		setMetricConfigurations(configuration.getMetricConfigurations());
	}

	private void setMetricConfigurations(Collection<MetricConfiguration> metricConfigurations) {
		this.metricConfigurations = new ArrayList<MetricConfigurationRecord>();
		for (MetricConfiguration metricConfiguration : metricConfigurations)
			this.metricConfigurations.add(new MetricConfigurationRecord(metricConfiguration, id));
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}
}