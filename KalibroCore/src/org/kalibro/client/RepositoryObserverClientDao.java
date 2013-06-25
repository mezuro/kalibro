package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.RepositoryObserver;
import org.kalibro.dao.RepositoryObserverDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.RepositoryObserverEndpoint;
import org.kalibro.service.xml.RepositoryObserverXml;

public class RepositoryObserverClientDao extends EndpointClient<RepositoryObserverEndpoint>
	implements RepositoryObserverDao {

	public RepositoryObserverClientDao(String serviceAddress) {
		super(serviceAddress, RepositoryObserverEndpoint.class);
	}

	@Override
	public SortedSet<RepositoryObserver> all() {
		return DataTransferObject.toSortedSet(port.allRepositoryObservers());
	}

	@Override
	public Long save(RepositoryObserver repositoryObserver, Long repositoryId) {
		return port.saveRepositoryObserver(new RepositoryObserverXml(repositoryObserver), repositoryId);
	}

	@Override
	public void delete(Long repositoryObserverId) {
		port.deleteRepositoryObserver(repositoryObserverId);
	}
}