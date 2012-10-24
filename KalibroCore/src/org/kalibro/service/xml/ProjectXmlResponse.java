package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Project;
import org.kalibro.dto.ProjectDto;

/**
 * XML element for {@link Project} responses.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectXmlResponse extends ProjectDto {

	@XmlElement
	private Long id;

	@XmlElement
	private String name;

	@XmlElement
	private String description;

	public ProjectXmlResponse() {
		super();
	}

	public ProjectXmlResponse(Project project) {
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