package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.RepositoryObserver;
import org.kalibro.core.persistence.record.RepositoryObserverRecord;
import org.kalibro.dao.RepositoryObserverDao;
import org.kalibro.dto.DataTransferObject;

public class RepositoryObserverDatabaseDao extends
	DatabaseDao<RepositoryObserver, RepositoryObserverRecord> implements RepositoryObserverDao {

	RepositoryObserverDatabaseDao() {
		super(RepositoryObserverRecord.class);
	}

	public SortedSet<RepositoryObserver> observersOf(Long repositoryId) {
		TypedQuery<RepositoryObserverRecord> query =
			createRecordQuery("repositoryObserver.repository = :repository");
		query.setParameter("repository", repositoryId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(RepositoryObserver repositoryObserver, Long repositoryId) {
		return save(new RepositoryObserverRecord(repositoryObserver, repositoryId)).id();
	}
}
