package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.ProcessingNotification;

public interface ProcessingNotificationDao {

	SortedSet<ProcessingNotification> all();

	Long save(ProcessingNotification processingNotification, Long repositoryId);

	void delete(Long processingNotificationId);
}