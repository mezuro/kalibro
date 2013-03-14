package org.kalibro.core.persistence.record;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "project")
	@TableGenerator(name = "project", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "project", initialValue = 1, allocationSize = 1)
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