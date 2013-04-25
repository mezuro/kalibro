package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingNotificationDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.ProcessingNotificationXml;

@WebService(name = "ProcessingNotificationEndpoint", serviceName = "ProcessingNotificationEndpointService")
public class ProcessingNotificationEndpointImpl
	implements ProcessingNotificationEndpoint {

	private ProcessingNotificationDao dao;

	public ProcessingNotificationEndpointImpl() {
		this(DaoFactory.getProcessingNotificationDao());
	}

	public ProcessingNotificationEndpointImpl(ProcessingNotificationDao processingNotificationDao) {
		dao = processingNotificationDao;
	}

	@Override
	@WebResult(name = "processingNotification")
	public List<ProcessingNotificationXml> allProcessingNotifications() {
		return DataTransferObject.createDtos(dao.all(), ProcessingNotificationXml.class);
	}

	@Override
	@WebMethod
	@WebResult(name = "processingNotificationId")
	public Long saveProcessingNotification(
		@WebParam(name = "processingNotification") ProcessingNotificationXml processingNotification, @WebParam(
			name = "repositoryId") Long repositoryId) {
		return dao.save(processingNotification.convert(), repositoryId);
	}

	@Override
	@WebMethod
	public void
		deleteProcessingNotification(@WebParam(name = "processingNotificationId") Long processingNotificationId) {
		dao.delete(processingNotificationId);
	}

}
