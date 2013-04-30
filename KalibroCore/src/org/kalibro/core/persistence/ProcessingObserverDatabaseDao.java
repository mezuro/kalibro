package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.ProcessingObserver;
import org.kalibro.core.persistence.record.ProcessingObserverRecord;
import org.kalibro.dao.ProcessingObserverDao;
import org.kalibro.dto.DataTransferObject;

public class ProcessingObserverDatabaseDao extends
	DatabaseDao<ProcessingObserver, ProcessingObserverRecord> implements ProcessingObserverDao {

	ProcessingObserverDatabaseDao() {
		super(ProcessingObserverRecord.class);
	}

	public SortedSet<ProcessingObserver> observersOf(Long repositoryId) {
		TypedQuery<ProcessingObserverRecord> query = 
			createRecordQuery("processingObserver.repository = :repository");
		query.setParameter("repository", repositoryId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(ProcessingObserver processingObserver, Long repositoryId) {
		return save(new ProcessingObserverRecord(processingObserver, repositoryId)).id();
	}
}