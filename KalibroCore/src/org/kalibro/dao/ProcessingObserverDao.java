package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.ProcessingObserver;

public interface ProcessingObserverDao {

	SortedSet<ProcessingObserver> all();

	Long save(ProcessingObserver processingNotification, Long repositoryId);

	void delete(Long processingNotificationId);
}