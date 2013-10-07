package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.RepositoryListenerDao;
import org.kalibro.service.xml.RepositoryListenerXml;

/**
 * End point to make {@link RepositoryListenerDao} interface available as Web service.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@WebService(name = "RepositoryListenerEndpoint", serviceName = "RepositoryListenerEndpointService")
public interface RepositoryListenerEndpoint {

	@WebMethod
	@WebResult(name = "repositoryListener")
	List<RepositoryListenerXml> allRepositoryListeners();

	@WebMethod
	@WebResult(name = "repositoryListenerId")
	Long saveRepositoryListener(
		@WebParam(name = "repositoryListener") RepositoryListenerXml repositoryListener,
		@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	void deleteRepositoryListener(@WebParam(name = "repositoryListenerId") Long repositoryListenerId);

	@WebMethod
	@WebResult(name = "repositoryListener")
	List<RepositoryListenerXml> repositoryListenersOf(@WebParam(name = "repositoryListenerId") Long repositoryId);
}
