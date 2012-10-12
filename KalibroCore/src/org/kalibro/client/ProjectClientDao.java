package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.Project;
import org.kalibro.dao.ProjectDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.xml.ProjectXmlRequest;

/**
 * {@link ProjectEndpoint} client implementation of {@link ProjectDao}.
 * 
 * @author Carlos Morais
 */
class ProjectClientDao extends EndpointClient<ProjectEndpoint> implements ProjectDao {

	ProjectClientDao(String serviceAddress) {
		super(serviceAddress, ProjectEndpoint.class);
	}

	@Override
	public boolean exists(Long projectId) {
		return port.projectExists(projectId);
	}

	@Override
	public Project get(Long projectId) {
		return port.getProject(projectId).convert();
	}

	@Override
	public SortedSet<Project> all() {
		return DataTransferObject.toSortedSet(port.allProjects());
	}

	@Override
	public Long save(Project project) {
		return port.saveProject(new ProjectXmlRequest(project));
	}

	@Override
	public void delete(Long projectId) {
		port.deleteProject(projectId);
	}
}