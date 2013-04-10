package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.ProcessingNotification;
import org.kalibro.Repository;

public interface ProcessingNotificationDao {

	SortedSet<ProcessingNotification> notificationsOf(Repository repository);
	
	SortedSet<ProcessingNotification> all();
}