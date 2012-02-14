package org.kalibro.client.dao;

import java.util.List;

import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.entities.RawProjectXml;

public class ProjectPortDao implements ProjectDao {

	protected ProjectEndpoint port;

	public ProjectPortDao() {
		port = EndpointPortFactory.getEndpointPort(ProjectEndpoint.class);
	}

	@Override
	public void save(Project project) {
		port.saveProject(new RawProjectXml(project));
	}

	@Override
	public List<String> getProjectNames() {
		return port.getProjectNames();
	}

	@Override
	public Project getProject(String projectName) {
		return port.getProject(projectName).convert();
	}

	@Override
	public void removeProject(String projectName) {
		port.removeProject(projectName);
	}
}