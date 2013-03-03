package org.kalibro.core.persistence.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kalibro.Project;
import org.kalibro.dto.ProjectDto;

/**
 * Java Persistence API entity for {@link Project}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Project")
@Table(name = "\"project\"")
public class ProjectRecord extends ProjectDto {

	@Id
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"name\"", nullable = false, unique = true)
	private String name;

	@Column(name = "\"description\"")
	private String description;

	public ProjectRecord() {
		super();
	}

	public ProjectRecord(Project project) {
		id = project.getId();
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