package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.dto.ProjectDto;

/**
 * Java Persistence API entity for {@link Project}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Project")
@Table(name = "\"PROJECT\"")
public class ProjectRecord extends ProjectDto {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	private Long id;

	@Column(name = "\"name\"", nullable = false, unique = true)
	private String name;

	@Column(name = "\"description\"")
	private String description;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
	private Collection<RepositoryRecord> repositories;

	public ProjectRecord() {
		super();
	}

	public ProjectRecord(Long id) {
		this.id = id;
	}

	public ProjectRecord(Project project) {
		this(project.getId());
		name = project.getName();
		description = project.getDescription();
		setRepositories(project.getRepositories());
	}

	private void setRepositories(Collection<Repository> repositories) {
		this.repositories = new ArrayList<RepositoryRecord>();
		for (Repository repository : repositories)
			this.repositories.add(new RepositoryRecord(repository, this));
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