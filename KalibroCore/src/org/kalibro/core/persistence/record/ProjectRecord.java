package org.kalibro.core.persistence.record;

import static org.kalibro.ResultState.ERROR;

import javax.persistence.*;

import org.kalibro.Configuration;
import org.kalibro.Project;
import org.kalibro.ResultState;
import org.kalibro.dto.DataTransferObject;

@Entity(name = "Project")
@Table(name = "\"PROJECT\"")
public class ProjectRecord extends DataTransferObject<Project> {

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
	@JoinColumn(nullable = false, referencedColumnName = "id")
	private ConfigurationRecord configuration;

	public ProjectRecord() {
		super();
	}

	public ProjectRecord(Project project) {
		this(project, null);
	}

	public ProjectRecord(Project project, Long configurationId) {
		id = project.getId();
		name = project.getName();
		license = project.getLicense();
		description = project.getDescription();
		repository = new RepositoryRecord(project.getRepository(), this);
		initializeConfiguration(configurationId);
		initializeState(project);
		initializeError(project);
	}

	private void initializeConfiguration(Long configurationId) {
		Configuration entity = new Configuration();
		setId(entity, configurationId);
		configuration = new ConfigurationRecord(entity);
	}

	private void initializeState(Project project) {
		ResultState resultState = project.getState();
		if (resultState == ERROR)
			resultState = project.getStateWhenErrorOcurred();
		state = resultState.name();
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
		project.setConfigurationName(configuration.name());
		project.setState(ResultState.valueOf(state));
		project.setError(error);
		return project;
	}
}