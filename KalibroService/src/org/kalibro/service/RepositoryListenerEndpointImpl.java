package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryListenerDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.RepositoryListenerXml;

/**
 * Implementation of {@link RepositoryListenerEndpoint}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@WebService(name = "RepositoryListenerEndpoint", serviceName = "RepositoryListenerEndpointService")
public class RepositoryListenerEndpointImpl
	implements RepositoryListenerEndpoint {

	private RepositoryListenerDao dao;

	public RepositoryListenerEndpointImpl() {
		this(DaoFactory.getRepositoryListenerDao());
	}

	public RepositoryListenerEndpointImpl(RepositoryListenerDao repositoryListenerDao) {
		dao = repositoryListenerDao;
	}

	@Override
	@WebResult(name = "repositoryListener")
	public List<RepositoryListenerXml> allRepositoryListeners() {
		return DataTransferObject.createDtos(dao.all(), RepositoryListenerXml.class);
	}

	@Override
	@WebMethod
	@WebResult(name = "repositoryListenerId")
	public Long saveRepositoryListener(
		@WebParam(name = "repositoryListener") RepositoryListenerXml repositoryListener, @WebParam(
			name = "repositoryId") Long repositoryId) {
		return dao.save(repositoryListener.convert(), repositoryId);
	}

	@Override
	@WebMethod
	public void
		deleteRepositoryListener(@WebParam(name = "repositoryListenerId") Long repositoryListenerId) {
		dao.delete(repositoryListenerId);
	}

	@Override
	@WebMethod
	@WebResult(name = "repositoryListener")
	public List<RepositoryListenerXml> repositoryListenersOf(
		@WebParam(name = "repositoryListenerId") Long repositoryId) {
		return DataTransferObject.createDtos(dao.listenersOf(repositoryId), RepositoryListenerXml.class);
	}
}
