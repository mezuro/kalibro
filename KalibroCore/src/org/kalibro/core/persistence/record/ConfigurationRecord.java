package org.kalibro.core.persistence.record;

import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.PrivateOwned;
import org.kalibro.Configuration;
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

	@PrivateOwned
	@OneToMany(mappedBy = "configuration", orphanRemoval = true)
	@SuppressWarnings("unused" /* used by JPA */)
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