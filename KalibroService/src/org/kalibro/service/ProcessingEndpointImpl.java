package org.kalibro.service;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.ProcessState;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.service.xml.ProcessingXml;

/**
 * Implementation of {@link ProcessingEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "ProcessingEndpoint", serviceName = "ProcessingEndpointService")
public class ProcessingEndpointImpl implements ProcessingEndpoint {

	private ProcessingDao dao;

	public ProcessingEndpointImpl() {
		this(DaoFactory.getProcessingDao());
	}

	public ProcessingEndpointImpl(ProcessingDao processingDao) {
		dao = processingDao;
	}

	@Override
	@WebResult(name = "exists")
	public boolean hasProcessing(@WebParam(name = "repositoryId") Long repositoryId) {
		return dao.hasProcessing(repositoryId);
	}

	@Override
	@WebResult(name = "exists")
	public boolean hasReadyProcessing(@WebParam(name = "repositoryId") Long repositoryId) {
		return dao.hasReadyProcessing(repositoryId);
	}

	@Override
	@WebResult(name = "exists")
	public boolean hasProcessingAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "repositoryId") Long repositoryId) {
		return dao.hasProcessingAfter(date, repositoryId);
	}

	@Override
	@WebResult(name = "exists")
	public boolean hasProcessingBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "repositoryId") Long repositoryId) {
		return dao.hasProcessingBefore(date, repositoryId);
	}

	@Override
	@WebResult(name = "processState")
	public ProcessState lastProcessingState(@WebParam(name = "repositoryId") Long repositoryId) {
		return dao.lastProcessingState(repositoryId);
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml lastReadyProcessing(@WebParam(name = "repositoryId") Long repositoryId) {
		return new ProcessingXml(dao.lastReadyProcessing(repositoryId));
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml firstProcessing(@WebParam(name = "repositoryId") Long repositoryId) {
		return new ProcessingXml(dao.firstProcessing(repositoryId));
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml lastProcessing(@WebParam(name = "repositoryId") Long repositoryId) {
		return new ProcessingXml(dao.lastProcessing(repositoryId));
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml firstProcessingAfter(
		@WebParam(name = "date") Date date,
		@WebParam(name = "repositoryId") Long repositoryId) {
		return new ProcessingXml(dao.firstProcessingAfter(date, repositoryId));
	}

	@Override
	@WebResult(name = "processing")
	public ProcessingXml lastProcessingBefore(
		@WebParam(name = "date") Date date,
		@WebParam(name = "repositoryId") Long repositoryId) {
		return new ProcessingXml(dao.lastProcessingBefore(date, repositoryId));
	}
}