package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.RepositorySubscriber;
import org.kalibro.dto.RepositorySubscriberDto;

/**
 * Java Persistence API entity for {@link RepositorySubscriber}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@Entity(name = "RepositorySubscriber")
@Table(name = "\"repository_subscriber\"")
public class RepositorySubscriberRecord extends RepositorySubscriberDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "repository_subscriber")
	@TableGenerator(name = "repository_subscriber", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "repository_subscriber", initialValue = 1,
		allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"repository\"", nullable = false)
	private Long repository;

	@Column(name = "\"name\"", nullable = false)
	private String name;

	@Column(name = "\"email\"", nullable = false)
	private String email;

	public RepositorySubscriberRecord() {
		super();
	}

	public RepositorySubscriberRecord(RepositorySubscriber repositorySubscriber) {
		this(repositorySubscriber, null);
	}

	public RepositorySubscriberRecord(RepositorySubscriber repositorySubscriber, Long repositoryId) {
		id = repositorySubscriber.getId();
		this.repository = repositoryId;
		name = repositorySubscriber.getName();
		email = repositorySubscriber.getEmail();
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
