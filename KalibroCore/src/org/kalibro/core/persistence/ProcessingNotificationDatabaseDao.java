package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.ProcessingNotification;
import org.kalibro.core.persistence.record.ProcessingNotificationRecord;
import org.kalibro.dao.ProcessingNotificationDao;
import org.kalibro.dto.DataTransferObject;

public class ProcessingNotificationDatabaseDao extends
	DatabaseDao<ProcessingNotification, ProcessingNotificationRecord> implements ProcessingNotificationDao {

	ProcessingNotificationDatabaseDao() {
		super(ProcessingNotificationRecord.class);
	}

	@Override
	public SortedSet<ProcessingNotification> notificationsOf(Long repositoryId) {
		TypedQuery<ProcessingNotificationRecord> query = createRecordQuery("repository.id = :repositoryId");
		query.setParameter("repositoryId", repositoryId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}
}