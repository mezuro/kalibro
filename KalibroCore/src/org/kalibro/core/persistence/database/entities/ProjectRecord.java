package org.kalibro.core.persistence.database.entities;

import static org.kalibro.core.model.enums.ProjectState.*;

import javax.persistence.*;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.util.DataTransferObject;

@Entity(name = "Project")
public class ProjectRecord implements DataTransferObject<Project> {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String license;

	@Column
	private String description;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "project", optional = false, orphanRemoval = true)
	private RepositoryRecord repository;

	@Column(nullable = false)
	private String state;

	@Column
	private Throwable error;

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, referencedColumnName = "name")
	private ConfigurationRecord configuration;

	public ProjectRecord() {
		super();
	}

	public ProjectRecord(Project project) {
		id = project.getId();
		name = project.getName();
		license = project.getLicense();
		description = project.getDescription();
		repository = new RepositoryRecord(project.getRepository(), this);
		initializeConfiguration(project);
		initializeState(project);
		initializeError(project);
	}

	private void initializeConfiguration(Project project) {
		Configuration entity = new Configuration();
		entity.setName(project.getConfigurationName());
		configuration = new ConfigurationRecord(entity);
	}

	private void initializeState(Project project) {
		ProjectState projectState = project.getState();
		if (projectState == ERROR)
			projectState = project.getStateWhenErrorOcurred();
		state = projectState.name();
	}

	private void initializeError(Project project) {
		if (project.getState() == ERROR)
			error = project.getError();
	}

	@Override
	public Project convert() {
		Project project = new Project();
		project.setId(id);
		project.setName(name);
		project.setLicense(license);
		project.setDescription(description);
		project.setRepository(repository.convert());
		project.setConfigurationName(configuration.getName());
		project.setState(ProjectState.valueOf(state));
		project.setError(error);
		return project;
	}
}