package org.kalibro.service;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.service.xml.ProcessingXml;

@WebService(name = "ProjectResultEndpoint", serviceName = "ProjectResultEndpointService")
public class ProcessingEndpointImpl implements ProjectResultEndpoint {

	private ProcessingDao dao;

	public ProcessingEndpointImpl() {
		this(DaoFactory.getProjectResultDao());
	}

	public ProcessingEndpointImpl(ProcessingDao processingDao) {
		dao = processingDao;
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
	@WebResult(name = "processing")
	public ProcessingXml getFirstResultOf(@WebParam(name = "projectName") String projectName) {
		return new ProcessingXml(dao.getFirstResultOf(projectName));
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml getLastResultOf(@WebParam(name = "projectName") String projectName) {
		return new ProcessingXml(dao.getLastResultOf(projectName));
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml getLastResultBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return new ProcessingXml(dao.getLastResultBefore(date, projectName));
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml getFirstResultAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "projectName") String projectName) {
		return new ProcessingXml(dao.getFirstResultAfter(date, projectName));
	}
}