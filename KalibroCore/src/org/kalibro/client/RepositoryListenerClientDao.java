package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.RepositoryListener;
import org.kalibro.dao.RepositoryListenerDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.RepositoryListenerEndpoint;
import org.kalibro.service.xml.RepositoryListenerXml;

/**
 * {@link RepositoryListenerEndpoint} client implementation of {@link RepositoryListenerDao}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public class RepositoryListenerClientDao extends EndpointClient<RepositoryListenerEndpoint>
	implements RepositoryListenerDao {

	public RepositoryListenerClientDao(String serviceAddress) {
		super(serviceAddress, RepositoryListenerEndpoint.class);
	}

	@Override
	public SortedSet<RepositoryListener> all() {
		return DataTransferObject.toSortedSet(port.allRepositoryListeners());
	}

	@Override
	public Long save(RepositoryListener repositoryListener, Long repositoryId) {
		return port.saveRepositoryListener(new RepositoryListenerXml(repositoryListener), repositoryId);
	}

	@Override
	public void delete(Long repositoryListenerId) {
		port.deleteRepositoryListener(repositoryListenerId);
	}

	@Override
	public SortedSet<RepositoryListener> listenersOf(Long repositoryId) {
		return DataTransferObject.toSortedSet(port.repositoryListenersOf(repositoryId));
	}
}