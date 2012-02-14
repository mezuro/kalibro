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
	@Column(name = "name", nullable = false)
	private String name;

	@Column(nullable = false)
	private String license;

	@Column
	private String description;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "project", optional = false, orphanRemoval = true)
	private RepositoryRecord repository;

	@Column(nullable = false)
	private String state;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
	private ErrorRecord error;

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, referencedColumnName = "name")
	private ConfigurationRecord configuration;

	public ProjectRecord() {
		super();
	}

	public ProjectRecord(Project project) {
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
			error = new ErrorRecord(project.getError(), this);
	}

	@Override
	public Project convert() {
		Project project = new Project();
		project.setName(name);
		project.setLicense(license);
		project.setDescription(description);
		project.setRepository(repository.convert());
		project.setConfigurationName(configuration.getName());
		convertState(project);
		convertError(project);
		return project;
	}

	private void convertState(Project project) {
		ProjectState projectState = ProjectState.valueOf(state);
		if (projectState != ERROR)
			project.setState(projectState);
	}

	private void convertError(Project project) {
		if (error != null)
			project.setError(error.convert());
	}
}