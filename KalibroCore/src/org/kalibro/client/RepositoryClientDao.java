package org.kalibro.client;

import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.RepositoryEndpoint;
import org.kalibro.service.xml.RepositoryXmlRequest;

/**
 * {@link RepositoryEndpoint} client implementation of {@link RepositoryDao}.
 * 
 * @author Carlos Morais
 */
class RepositoryClientDao extends EndpointClient<RepositoryEndpoint> implements RepositoryDao {

	RepositoryClientDao(String serviceAddress) {
		super(serviceAddress, RepositoryEndpoint.class);
	}

	@Override
	public SortedSet<RepositoryType> supportedTypes() {
		return new TreeSet<RepositoryType>(port.supportedRepositoryTypes());
	}

	@Override
	public Repository repositoryOf(Long processingId) {
		return port.repositoryOf(processingId).convert();
	}

	@Override
	public SortedSet<Repository> repositoriesOf(Long projectId) {
		return DataTransferObject.toSortedSet(port.repositoriesOf(projectId));
	}

	@Override
	public Long save(Repository repository, Long projectId) {
		return port.saveRepository(new RepositoryXmlRequest(repository), projectId);
	}

	@Override
	public void process(Long repositoryId) {
		port.processRepository(repositoryId);
	}

	@Override
	public void cancelProcessing(Long repositoryId) {
		port.cancelProcessingOfRepository(repositoryId);
	}

	@Override
	public void delete(Long repositoryId) {
		port.deleteRepository(repositoryId);
	}
}