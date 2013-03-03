package org.kalibro.core.persistence.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kalibro.Configuration;
import org.kalibro.dto.ConfigurationDto;

/**
 * Java Persistence API entity for {@link Configuration}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Configuration")
@Table(name = "\"configuration\"")
public class ConfigurationRecord extends ConfigurationDto {

	@Id
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"name\"", nullable = false, unique = true)
	private String name;

	@Column(name = "\"description\"")
	private String description;

	public ConfigurationRecord() {
		super();
	}

	public ConfigurationRecord(Configuration configuration) {
		id = configuration.getId();
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