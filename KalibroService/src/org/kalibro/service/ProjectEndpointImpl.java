package org.kalibro.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.service.entities.ProjectXml;
import org.kalibro.service.entities.RawProjectXml;

@WebService(name = "ProjectEndpoint", serviceName = "ProjectEndpointService")
public class ProjectEndpointImpl implements ProjectEndpoint {

	private ProjectDao dao;

	public ProjectEndpointImpl() {
		this(DaoFactory.getProjectDao());
	}

	protected ProjectEndpointImpl(ProjectDao projectDao) {
		dao = projectDao;
	}

	@Override
	public void saveProject(@WebParam(name = "project") RawProjectXml project) {
		dao.save(project.convert());
	}

	@Override
	@WebResult(name = "projectName")
	public List<String> getProjectNames() {
		return dao.getProjectNames();
	}

	@Override
	@WebResult(name = "hasProject")
	public boolean hasProject(@WebParam(name = "projectName") String projectName) {
		return dao.hasProject(projectName);
	}

	@Override
	@WebResult(name = "project")
	public ProjectXml getProject(@WebParam(name = "projectName") String projectName) {
		return new ProjectXml(dao.getProject(projectName));
	}

	@Override
	public void removeProject(@WebParam(name = "projectName") String projectName) {
		dao.removeProject(projectName);
	}

	@Override
	@WebResult(name = "repositoryType")
	public Set<RepositoryType> getSupportedRepositoryTypes() {
		Set<RepositoryType> types = new TreeSet<RepositoryType>();
		for (RepositoryType type : dao.getSupportedRepositoryTypes())
			if (!type.isLocal())
				types.add(type);
		return types;
	}

	@Override
	public void processProject(@WebParam(name = "projectName") String projectName) {
		dao.processProject(projectName);
	}

	@Override
	public void processPeriodically(
		@WebParam(name = "projectName") String projectName,
		@WebParam(name = "periodInDays") Integer periodInDays) {
		dao.processPeriodically(projectName, periodInDays);
	}

	@Override
	@WebResult(name = "period")
	public Integer getProcessPeriod(@WebParam(name = "projectName") String projectName) {
		return dao.getProcessPeriod(projectName);
	}

	@Override
	public void cancelPeriodicProcess(@WebParam(name = "projectName") String projectName) {
		dao.cancelPeriodicProcess(projectName);
	}
}