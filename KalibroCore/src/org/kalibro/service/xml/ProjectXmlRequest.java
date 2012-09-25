package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Project;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectXmlRequest extends DataTransferObject<Project> {

	@XmlElement(required = true)
	private String name;

	private String license;
	private String description;

	@XmlElement(required = true)
	private RepositoryXml repository;

	@XmlElement(required = true)
	private String configurationName;

	public ProjectXmlRequest() {
		super();
	}

	public ProjectXmlRequest(Project project) {
		name = project.getName();
		license = project.getLicense();
		description = project.getDescription();
		repository = new RepositoryXml(project.getRepository());
		configurationName = project.getConfigurationName();
	}

	@Override
	public Project convert() {
		Project project = new Project();
		project.setName(name);
		project.setLicense(license);
		project.setDescription(description);
		project.setRepository(repository.convert());
		project.setConfigurationName(configurationName);
		return project;
	}
}