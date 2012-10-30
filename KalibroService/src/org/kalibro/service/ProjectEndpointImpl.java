package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.ProjectXml;

/**
 * Implementation of {@link ProjectEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ProjectEndpoint", serviceName = "ProjectEndpointService")
public class ProjectEndpointImpl implements ProjectEndpoint {

	private ProjectDao dao;

	public ProjectEndpointImpl() {
		this(DaoFactory.getProjectDao());
	}

	public ProjectEndpointImpl(ProjectDao projectDao) {
		dao = projectDao;
	}

	@Override
	@WebResult(name = "exists")
	public boolean projectExists(@WebParam(name = "projectId") Long projectId) {
		return dao.exists(projectId);
	}

	@Override
	@WebResult(name = "project")
	public ProjectXml getProject(@WebParam(name = "projectId") Long projectId) {
		return new ProjectXml(dao.get(projectId));
	}

	@Override
	@WebResult(name = "project")
	public List<ProjectXml> allProjects() {
		return DataTransferObject.createDtos(dao.all(), ProjectXml.class);
	}

	@Override
	@WebResult(name = "projectId")
	public Long saveProject(@WebParam(name = "project") ProjectXml project) {
		return dao.save(project.convert());
	}

	@Override
	public void deleteProject(@WebParam(name = "projectId") Long projectId) {
		dao.delete(projectId);
	}
}