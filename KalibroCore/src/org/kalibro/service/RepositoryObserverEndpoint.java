package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.RepositoryObserverXml;

@WebService(name = "RepositoryObserverEndpoint", serviceName = "RepositoryObserverEndpointService")
public interface RepositoryObserverEndpoint {

	@WebMethod
	@WebResult(name = "repositoryObserver")
	List<RepositoryObserverXml> allRepositoryObservers();

	@WebMethod
	@WebResult(name = "repositoryObserverId")
	Long saveRepositoryObserver(
		@WebParam(name = "repositoryObserver") RepositoryObserverXml repositoryObserver,
		@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	void deleteRepositoryObserver(@WebParam(name = "repositoryObserverId") Long repositoryObserverId);
}
