package org.kalibro.service.xml;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.dto.ProjectDto;

/**
 * XML element for {@link Project} requests.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectXmlRequest extends ProjectDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement
	private String description;

	@XmlElement(name = "repository")
	private Collection<RepositoryXmlRequest> repositories;

	public ProjectXmlRequest() {
		super();
	}

	public ProjectXmlRequest(Project project) {
		id = project.getId();
		name = project.getName();
		description = project.getDescription();
		repositories = createDtos(project.getRepositories(), RepositoryXmlRequest.class);
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

	@Override
	public SortedSet<Repository> repositories() {
		return repositories == null ? new TreeSet<Repository>() : toSortedSet(repositories);
	}
}