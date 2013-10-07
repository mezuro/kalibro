package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.RepositoryListener;
import org.kalibro.dto.RepositoryListenerDto;

/**
 * Java Persistence API entity for {@link RepositoryListener}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@Entity(name = "RepositoryListener")
@Table(name = "\"repository_listener\"")
public class RepositoryListenerRecord extends RepositoryListenerDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "repository_listener")
	@TableGenerator(name = "repository_listener", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "repository_listener", initialValue = 1,
		allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"repository\"", nullable = false)
	private Long repository;

	@Column(name = "\"name\"", nullable = false)
	private String name;

	@Column(name = "\"email\"", nullable = false)
	private String email;

	public RepositoryListenerRecord() {
		super();
	}

	public RepositoryListenerRecord(RepositoryListener repositoryListener) {
		this(repositoryListener, null);
	}

	public RepositoryListenerRecord(RepositoryListener repositoryListener, Long repositoryId) {
		id = repositoryListener.getId();
		this.repository = repositoryId;
		name = repositoryListener.getName();
		email = repositoryListener.getEmail();
	}

	@Override
	public Long id() {
		return id;
	}

	public Long repository() {
		return repository;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String email() {
		return email;
	}

}
