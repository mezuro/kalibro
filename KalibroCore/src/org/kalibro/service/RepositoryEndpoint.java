package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.RepositoryType;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.service.xml.RepositoryXmlRequest;
import org.kalibro.service.xml.RepositoryXmlResponse;

/**
 * End point to make {@link RepositoryDao} interface available as Web service.
 * 
 * @author Carlos Morais
 */
@WebService(name = "RepositoryEndpoint", serviceName = "RepositoryEndpointService")
public interface RepositoryEndpoint {

	@WebMethod
	@WebResult(name = "supportedType")
	List<RepositoryType> supportedRepositoryTypes();

	@WebMethod
	@WebResult(name = "repository")
	RepositoryXmlResponse repositoryOf(@WebParam(name = "processingId") Long processingId);

	@WebMethod
	@WebResult(name = "repository")
	List<RepositoryXmlResponse> repositoriesOf(@WebParam(name = "projectId") Long projectId);

	@WebMethod
	@WebResult(name = "repositoryId")
	Long saveRepository(
		@WebParam(name = "repository") RepositoryXmlRequest repository,
		@WebParam(name = "projectId") Long projectId);

	@WebMethod
	void processRepository(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	void cancelProcessingOfRepository(@WebParam(name = "repositoryId") Long repositoryId);

	@WebMethod
	void deleteRepository(@WebParam(name = "repositoryId") Long repositoryId);
}