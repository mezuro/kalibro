package org.kalibro.core.persistence.record;

import java.util.Collection;

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
@Table(name = "\"REPOSITORY\"")
public class RepositoryRecord extends RepositoryDto {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"project\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ProjectRecord project;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "\"configuration\"", nullable = false, referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ConfigurationRecord configuration;

	@Column
	private Collection<String> mailsToNotify;

	public RepositoryRecord() {
		super();
	}

	public RepositoryRecord(Long id) {
		this.id = id;
	}

	public RepositoryRecord(Repository repository) {
		this(repository, (Long) null);
	}

	public RepositoryRecord(Repository repository, Long projectId) {
		this(repository, new ProjectRecord(projectId));
	}

	public RepositoryRecord(Repository repository, ProjectRecord projectRecord) {
		this(repository.getId());
		project = projectRecord;
		name = repository.getName();
		type = repository.getType().name();
		address = repository.getAddress();
		description = repository.getDescription();
		license = repository.getLicense();
		processPeriod = repository.getProcessPeriod();
		configuration = new ConfigurationRecord(repository.getConfiguration().getId());
		mailsToNotify = repository.getMailsToNotify();
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
	public Collection<String> mailsToNotify() {
		return mailsToNotify;
	}
}