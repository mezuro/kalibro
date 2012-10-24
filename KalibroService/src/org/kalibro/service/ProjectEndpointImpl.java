package org.kalibro.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.ProjectXmlRequest;
import org.kalibro.service.xml.ProjectXmlResponse;

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
	public ProjectXmlResponse getProject(@WebParam(name = "projectId") Long projectId) {
		return new ProjectXmlResponse(dao.get(projectId));
	}

	@Override
	@WebResult(name = "project")
	public List<ProjectXmlResponse> allProjects() {
		return DataTransferObject.createDtos(dao.all(), ProjectXmlResponse.class);
	}

	@Override
	@WebResult(name = "projectId")
	public Long saveProject(@WebParam(name = "project") ProjectXmlRequest project) {
		return dao.save(project.convert());
	}

	@Override
	public void deleteProject(@WebParam(name = "projectId") Long projectId) {
		dao.delete(projectId);
	}
}