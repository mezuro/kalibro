package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.ProcessingNotification;

public interface ProcessingNotificationDao {

	SortedSet<ProcessingNotification> notificationsOf(Long repositoryId);

	SortedSet<ProcessingNotification> all();

}