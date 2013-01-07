package org.kalibro.core.persistence.record;

import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.kalibro.Project;
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

	@CascadeOnDelete
	@OneToMany(mappedBy = "project", orphanRemoval = true)
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