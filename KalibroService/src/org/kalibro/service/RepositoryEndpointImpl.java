package org.kalibro.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.RepositoryType;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.xml.RepositoryXmlRequest;
import org.kalibro.service.xml.RepositoryXmlResponse;

/**
 * Implementation of {@link RepositoryEndpoint}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "RepositoryEndpoint", serviceName = "RepositoryEndpointService")
public class RepositoryEndpointImpl implements RepositoryEndpoint {

	private RepositoryDao dao;

	public RepositoryEndpointImpl() {
		this(DaoFactory.getRepositoryDao());
	}

	public RepositoryEndpointImpl(RepositoryDao repositoryDao) {
		dao = repositoryDao;
	}

	@Override
	@WebResult(name = "supportedType")
	public List<RepositoryType> supportedRepositoryTypes() {
		return new ArrayList<RepositoryType>(dao.supportedTypes());
	}

	@Override
	@WebResult(name = "repository")
	public RepositoryXmlResponse repositoryOf(@WebParam(name = "processingId") Long processingId) {
		return new RepositoryXmlResponse(dao.repositoryOf(processingId));
	}

	@Override
	@WebResult(name = "repository")
	public List<RepositoryXmlResponse> repositoriesOf(@WebParam(name = "projectId") Long projectId) {
		return DataTransferObject.createDtos(dao.repositoriesOf(projectId), RepositoryXmlResponse.class);
	}

	@Override
	@WebResult(name = "repositoryId")
	public Long saveRepository(
		@WebParam(name = "repository") RepositoryXmlRequest repository,
		@WebParam(name = "projectId") Long projectId) {
		return dao.save(repository.convert(), projectId);
	}

	@Override
	public void processRepository(@WebParam(name = "repositoryId") Long repositoryId) {
		dao.process(repositoryId);
	}

	@Override
	public void cancelProcessingOfRepository(@WebParam(name = "repositoryId") Long repositoryId) {
		dao.cancelProcessing(repositoryId);
	}

	@Override
	public void deleteRepository(@WebParam(name = "repositoryId") Long repositoryId) {
		dao.delete(repositoryId);
	}
}