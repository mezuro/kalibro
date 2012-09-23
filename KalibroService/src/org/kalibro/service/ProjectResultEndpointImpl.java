package org.kalibro.service;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectResultDao;
import org.kalibro.service.xml.ProjectResultXml;

@WebService(name = "ProjectResultEndpoint", serviceName = "ProjectResultEndpointService")
public class ProjectResultEndpointImpl implements ProjectResultEndpoint {

	private ProjectResultDao dao;

	public ProjectResultEndpointImpl() {
		this(DaoFactory.getProjectResultDao());
	}

	public ProjectResultEndpointImpl(ProjectResultDao projectResultDao) {
		dao = projectResultDao;
	}

	@Override
	@WebResult(name = "hasResults")
	public boolean hasResultsFor(@WebParam(name = "projectName") String projectName) {
		return dao.hasResultsFor(projectName);
	}

	@Override
	@WebResult(name = "hasResults")
	public boolean hasResultsBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return dao.hasResultsBefore(date, projectName);
	}

	@Override
	@WebResult(name = "hasResults")
	public boolean hasResultsAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return dao.hasResultsAfter(date, projectName);
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getFirstResultOf(@WebParam(name = "projectName") String projectName) {
		return new ProjectResultXml(dao.getFirstResultOf(projectName));
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getLastResultOf(@WebParam(name = "projectName") String projectName) {
		return new ProjectResultXml(dao.getLastResultOf(projectName));
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getLastResultBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return new ProjectResultXml(dao.getLastResultBefore(date, projectName));
	}

	@Override
	@WebResult(name = "projectResult")
	public ProjectResultXml getFirstResultAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return new ProjectResultXml(dao.getFirstResultAfter(date, projectName));
	}
}