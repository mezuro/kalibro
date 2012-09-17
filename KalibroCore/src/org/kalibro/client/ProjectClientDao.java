package org.kalibro.client;

import java.util.List;
import java.util.Set;

import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.dao.ProjectDao;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.entities.RawProjectXml;

class ProjectClientDao extends EndpointClient<ProjectEndpoint> implements ProjectDao {

	protected ProjectClientDao(String serviceAddress) {
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

	@Override
	public Set<RepositoryType> getSupportedRepositoryTypes() {
		return port.getSupportedRepositoryTypes();
	}

	@Override
	public void processProject(String projectName) {
		port.processProject(projectName);
	}

	@Override
	public void processPeriodically(String projectName, Integer periodInDays) {
		port.processPeriodically(projectName, periodInDays);
	}

	@Override
	public Integer getProcessPeriod(String projectName) {
		return port.getProcessPeriod(projectName);
	}

	@Override
	public void cancelPeriodicProcess(String projectName) {
		port.cancelPeriodicProcess(projectName);
	}
}