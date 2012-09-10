package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;

@Entity(name = "Repository")
public class RepositoryRecord extends DataTransferObject<Repository> {

	@Id
	@OneToOne(optional = false)
	@JoinColumn(nullable = false, referencedColumnName = "name")
	@SuppressWarnings("unused" /* used by JPA */)
	private ProjectRecord project;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private String address;

	@Column
	private String username;

	@Column
	private String password;

	public RepositoryRecord() {
		super();
	}

	public RepositoryRecord(Repository repository, ProjectRecord project) {
		this.project = project;
		type = repository.getType().name();
		address = repository.getAddress();
		username = repository.getUsername();
		password = repository.getPassword();
	}

	@Override
	public Repository convert() {
		return new Repository(RepositoryType.valueOf(type), address, username, password);
	}
}