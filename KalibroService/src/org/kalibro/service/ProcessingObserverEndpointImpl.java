package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingObserverDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.ProcessingObserverXml;

@WebService(name = "ProcessingObserverEndpoint", serviceName = "ProcessingObserverEndpointService")
public class ProcessingObserverEndpointImpl
	implements ProcessingObserverEndpoint {

	private ProcessingObserverDao dao;

	public ProcessingObserverEndpointImpl() {
		this(DaoFactory.getProcessingObserverDao());
	}

	public ProcessingObserverEndpointImpl(ProcessingObserverDao processingObserverDao) {
		dao = processingObserverDao;
	}

	@Override
	@WebResult(name = "processingObserver")
	public List<ProcessingObserverXml> allProcessingObservers() {
		return DataTransferObject.createDtos(dao.all(), ProcessingObserverXml.class);
	}

	@Override
	@WebMethod
	@WebResult(name = "processingObserverId")
	public Long saveProcessingObserver(
		@WebParam(name = "processingObserver") ProcessingObserverXml processingObserver, @WebParam(
			name = "repositoryId") Long repositoryId) {
		return dao.save(processingObserver.convert(), repositoryId);
	}

	@Override
	@WebMethod
	public void
		deleteProcessingObserver(@WebParam(name = "processingObserverId") Long processingObserverId) {
		dao.delete(processingObserverId);
	}

}
