package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.dao.RepositorySubscriberDao;
import org.kalibro.service.xml.RepositorySubscriberXml;

/**
 * End point to make {@link RepositorySubscriberDao} interface available as Web service.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@WebService(name = "RepositorySubscriberEndpoint", serviceName = "RepositorySubscriberEndpointService")
public interface RepositorySubscriberEndpoint {

	@WebMethod
	@WebResult(name = "repositorySubscriber")
	List<RepositorySubscriberXml> allRepositorySubscribers();

	@WebMethod
	@WebResult(name = "repositorySubscriberId")
	Long saveRepositorySubscriber(
		@WebParam(name = "repositorySubscriber") RepositorySubscriberXml repositorySubscriber,
		@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	void deleteRepositorySubscriber(@WebParam(name = "repositorySubscriberId") Long repositorySubscriberId);

	@WebMethod
	@WebResult(name = "repositorySubscriber")
	List<RepositorySubscriberXml> repositorySubscribersOf(@WebParam(name = "repositorySubscriberId") Long repositoryId);
}
