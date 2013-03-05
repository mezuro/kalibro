package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dto.RepositoryDto;

/**
 * Java Persistence API entity for {@link Repository}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Repository")
@Table(name = "\"repository\"")
public class RepositoryRecord extends RepositoryDto {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"project\"", nullable = false)
	private Long project;

	@Column(name = "\"name\"", nullable = false)
	private String name;

	@Column(name = "\"type\"", nullable = false)
	private String type;

	@Column(name = "\"address\"", nullable = false)
	private String address;

	@Column(name = "\"description\"")
	private String description;

	@Column(name = "\"license\"")
	private String license;

	@Column(name = "\"process_period\"")
	private Integer processPeriod;

	@Column(name = "\"configuration\"", nullable = false)
	private Long configuration;

	public RepositoryRecord() {
		super();
	}

	public RepositoryRecord(Repository repository) {
		this(repository, null);
	}

	public RepositoryRecord(Repository repository, Long projectId) {
		id = repository.getId();
		project = projectId;
		name = repository.getName();
		type = repository.getType().name();
		address = repository.getAddress();
		description = repository.getDescription();
		license = repository.getLicense();
		processPeriod = repository.getProcessPeriod();
		configuration = repository.getConfiguration().getId();
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
	public RepositoryType type() {
		return RepositoryType.valueOf(type);
	}

	@Override
	public String address() {
		return address;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public String license() {
		return license;
	}

	@Override
	public Integer processPeriod() {
		return processPeriod;
	}

	@Override
	public Long configurationId() {
		return configuration;
	}

	@Override
	public Long projectId() {
		return project;
	}
}