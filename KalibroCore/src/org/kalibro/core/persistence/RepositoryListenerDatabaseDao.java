package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.RepositoryListener;
import org.kalibro.core.persistence.record.RepositoryListenerRecord;
import org.kalibro.dao.RepositoryListenerDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link RepositoryListenerDao}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public class RepositoryListenerDatabaseDao extends
	DatabaseDao<RepositoryListener, RepositoryListenerRecord> implements RepositoryListenerDao {

	RepositoryListenerDatabaseDao() {
		super(RepositoryListenerRecord.class);
	}

	@Override
	public SortedSet<RepositoryListener> listenersOf(Long repositoryId) {
		TypedQuery<RepositoryListenerRecord> query =
			createRecordQuery("repositoryListener.repository = :repository");
		query.setParameter("repository", repositoryId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(RepositoryListener repositoryListener, Long repositoryId) {
		return save(new RepositoryListenerRecord(repositoryListener, repositoryId)).id();
	}
}
