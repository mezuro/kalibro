package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositorySubscriberDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.RepositorySubscriberXml;

/**
 * Implementation of {@link RepositorySubscriberEndpoint}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@WebService(name = "RepositorySubscriberEndpoint", serviceName = "RepositorySubscriberEndpointService")
public class RepositorySubscriberEndpointImpl
	implements RepositorySubscriberEndpoint {

	private RepositorySubscriberDao dao;

	public RepositorySubscriberEndpointImpl() {
		this(DaoFactory.getRepositorySubscriberDao());
	}

	public RepositorySubscriberEndpointImpl(RepositorySubscriberDao repositorySubscriberDao) {
		dao = repositorySubscriberDao;
	}

	@Override
	@WebResult(name = "repositorySubscriber")
	public List<RepositorySubscriberXml> allRepositorySubscribers() {
		return DataTransferObject.createDtos(dao.all(), RepositorySubscriberXml.class);
	}

	@Override
	@WebMethod
	@WebResult(name = "repositorySubscriberId")
	public Long saveRepositorySubscriber(
		@WebParam(name = "repositorySubscriber") RepositorySubscriberXml repositorySubscriber, @WebParam(
			name = "repositoryId") Long repositoryId) {
		return dao.save(repositorySubscriber.convert(), repositoryId);
	}

	@Override
	@WebMethod
	public void
		deleteRepositorySubscriber(@WebParam(name = "repositorySubscriberId") Long repositorySubscriberId) {
		dao.delete(repositorySubscriberId);
	}

	@Override
	@WebMethod
	@WebResult(name = "repositorySubscriber")
	public List<RepositorySubscriberXml> repositorySubscribersOf(
		@WebParam(name = "repositorySubscriberId") Long repositoryId) {
		return DataTransferObject.createDtos(dao.subscribersOf(repositoryId), RepositorySubscriberXml.class);
	}
}
