package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.RepositorySubscriber;
import org.kalibro.dao.RepositorySubscriberDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.RepositorySubscriberEndpoint;
import org.kalibro.service.xml.RepositorySubscriberXml;

/**
 * {@link RepositorySubscriberEndpoint} client implementation of {@link RepositorySubscriberDao}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public class RepositorySubscriberClientDao extends EndpointClient<RepositorySubscriberEndpoint>
	implements RepositorySubscriberDao {

	public RepositorySubscriberClientDao(String serviceAddress) {
		super(serviceAddress, RepositorySubscriberEndpoint.class);
	}

	@Override
	public SortedSet<RepositorySubscriber> all() {
		return DataTransferObject.toSortedSet(port.allRepositorySubscribers());
	}

	@Override
	public Long save(RepositorySubscriber repositorySubscriber, Long repositoryId) {
		return port.saveRepositorySubscriber(new RepositorySubscriberXml(repositorySubscriber), repositoryId);
	}

	@Override
	public void delete(Long repositorySubscriberId) {
		port.deleteRepositorySubscriber(repositorySubscriberId);
	}

	@Override
	public SortedSet<RepositorySubscriber> subscribersOf(Long repositoryId) {
		return DataTransferObject.toSortedSet(port.repositorySubscribersOf(repositoryId));
	}
}