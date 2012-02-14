package org.kalibro.service.entities;

import static org.kalibro.core.model.enums.ProjectState.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.util.DataTransferObject;

@XmlRootElement(name = "Project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectXml implements DataTransferObject<Project> {

	private String name;
	private String license;
	private String description;
	private RepositoryXml repository;

	private ProjectState state;
	private ErrorXml error;

	private String configurationName;

	public ProjectXml() {
		super();
	}

	public ProjectXml(Project project) {
		name = project.getName();
		license = project.getLicense();
		description = project.getDescription();
		repository = new RepositoryXml(project.getRepository());
		initializeState(project);
		initializeError(project);
		configurationName = project.getConfigurationName();
	}

	private void initializeState(Project project) {
		state = (project.getState() == ERROR) ? project.getStateWhenErrorOcurred() : project.getState();
	}

	private void initializeError(Project project) {
		if (project.getState() == ERROR)
			error = new ErrorXml(project.getError());
	}

	@Override
	public Project convert() {
		Project project = new Project();
		project.setName(name);
		project.setLicense(license);
		project.setDescription(description);
		project.setRepository(repository.convert());
		convertState(project);
		convertErrorReport(project);
		project.setConfigurationName(configurationName);
		return project;
	}

	private void convertState(Project project) {
		if (state != ERROR)
			project.setState(state);
	}

	private void convertErrorReport(Project project) {
		if (error != null)
			project.setError(error.convert());
	}
}