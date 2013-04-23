package org.kalibro.core.persistence;

import java.util.ArrayList;
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

	public SortedSet<ProcessingNotification> notificationsOf(Long repositoryId) {
		TypedQuery<ProcessingNotificationRecord> query = 
			createRecordQuery("processingNotification.repository = :repository");
		query.setParameter("repository", repositoryId);
		
		ArrayList<ProcessingNotification> notificationsList = query.getResultList();
		for (ProcessingNotification notification : notificationsList) {
			notifications.setRepository(repository);
		}
		
		return DataTransferObject.toSortedSet(
	}

	@Override
	public Long save(ProcessingNotification processingNotification, Long repositoryId) {
		return save(new ProcessingNotificationRecord(processingNotification, repositoryId)).id();
	}
}