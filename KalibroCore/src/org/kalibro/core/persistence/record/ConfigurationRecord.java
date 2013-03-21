package org.kalibro.core.persistence.record;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "configuration")
	@TableGenerator(name = "configuration", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "configuration", initialValue = 1, allocationSize = 1)
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