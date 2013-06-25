package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.RepositoryObserver;
import org.kalibro.dto.RepositoryObserverDto;

@Entity(name = "RepositoryObserver")
@Table(name = "\"repository_observer\"")
public class RepositoryObserverRecord extends RepositoryObserverDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "repository_observer")
	@TableGenerator(name = "repository_observer", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "repository_observer", initialValue = 1,
		allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"repository\"", nullable = false)
	private Long repository;

	@Column(name = "\"name\"", nullable = false)
	private String name;

	@Column(name = "\"email\"", nullable = false)
	private String email;

	public RepositoryObserverRecord() {
		super();
	}

	public RepositoryObserverRecord(RepositoryObserver repositoryObserver) {
		this(repositoryObserver, null);
	}

	public RepositoryObserverRecord(RepositoryObserver repositoryObserver, Long repositoryId) {
		id = repositoryObserver.getId();
		this.repository = repositoryId;
		name = repositoryObserver.getName();
		email = repositoryObserver.getEmail();
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
