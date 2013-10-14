package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.RepositorySubscriber;
import org.kalibro.core.persistence.record.RepositorySubscriberRecord;
import org.kalibro.dao.RepositorySubscriberDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link RepositorySubscriberDao}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public class RepositorySubscriberDatabaseDao extends
	DatabaseDao<RepositorySubscriber, RepositorySubscriberRecord> implements RepositorySubscriberDao {

	RepositorySubscriberDatabaseDao() {
		super(RepositorySubscriberRecord.class);
	}

	@Override
	public SortedSet<RepositorySubscriber> subscribersOf(Long repositoryId) {
		TypedQuery<RepositorySubscriberRecord> query =
			createRecordQuery("repositorySubscriber.repository = :repository");
		query.setParameter("repository", repositoryId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(RepositorySubscriber repositorySubscriber, Long repositoryId) {
		return save(new RepositorySubscriberRecord(repositorySubscriber, repositoryId)).id();
	}
}
