package org.kalibro.service.xml;

import static org.kalibro.RepositoryState.ERROR;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Project;
import org.kalibro.RepositoryState;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectXmlResponse extends DataTransferObject<Project> {

	private Long id;
	private String name;
	private String license;
	private String description;
	private RepositoryXml repository;

	private RepositoryState state;
	private ThrowableXml error;

	private String configurationName;

	public ProjectXmlResponse() {
		super();
	}

	public ProjectXmlResponse(Project project) {
		id = project.getId();
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
			error = new ThrowableXml(project.getError());
	}

	@Override
	public Project convert() {
		Project project = new Project();
		project.setId(id);
		project.setName(name);
		project.setLicense(license);
		project.setDescription(description);
		project.setRepository(repository.convert());
		project.setState(state);
		if (error != null)
			project.setError(error.convert());
		project.setConfigurationName(configurationName);
		return project;
	}
}