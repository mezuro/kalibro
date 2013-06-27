package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryObserverDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.RepositoryObserverXml;

/**
 * Implementation of {@link RepositoryObserverEndpoint}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@WebService(name = "RepositoryObserverEndpoint", serviceName = "RepositoryObserverEndpointService")
public class RepositoryObserverEndpointImpl
	implements RepositoryObserverEndpoint {

	private RepositoryObserverDao dao;

	public RepositoryObserverEndpointImpl() {
		this(DaoFactory.getRepositoryObserverDao());
	}

	public RepositoryObserverEndpointImpl(RepositoryObserverDao repositoryObserverDao) {
		dao = repositoryObserverDao;
	}

	@Override
	@WebResult(name = "repositoryObserver")
	public List<RepositoryObserverXml> allRepositoryObservers() {
		return DataTransferObject.createDtos(dao.all(), RepositoryObserverXml.class);
	}

	@Override
	@WebMethod
	@WebResult(name = "repositoryObserverId")
	public Long saveRepositoryObserver(
		@WebParam(name = "repositoryObserver") RepositoryObserverXml repositoryObserver, @WebParam(
			name = "repositoryId") Long repositoryId) {
		return dao.save(repositoryObserver.convert(), repositoryId);
	}

	@Override
	@WebMethod
	public void
		deleteRepositoryObserver(@WebParam(name = "repositoryObserverId") Long repositoryObserverId) {
		dao.delete(repositoryObserverId);
	}

	@Override
	@WebMethod
	@WebResult(name = "repositoryObserver")
	public List<RepositoryObserverXml> repositoryObserversOf(
		@WebParam(name = "repositoryObserverId") Long repositoryId) {
		return DataTransferObject.createDtos(dao.observersOf(repositoryId), RepositoryObserverXml.class);
	}
}
