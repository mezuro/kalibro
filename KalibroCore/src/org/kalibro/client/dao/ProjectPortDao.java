package org.kalibro.client.dao;

import java.util.List;

import org.kalibro.client.EndpointClient;
import org.kalibro.core.dao.ProjectDao;
import org.kalibro.core.model.Project;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.entities.RawProjectXml;

class ProjectPortDao extends EndpointClient<ProjectEndpoint> implements ProjectDao {

	protected ProjectPortDao(String serviceAddress) {
		super(serviceAddress, ProjectEndpoint.class);
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
	public boolean hasProject(String projectName) {
		return port.hasProject(projectName);
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