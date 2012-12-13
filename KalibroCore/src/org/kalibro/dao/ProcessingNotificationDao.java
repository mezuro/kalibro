package org.kalibro.dao;

import java.util.List;

import org.kalibro.ProcessingNotification;
import org.kalibro.Repository;


public interface ProcessingNotificationDao {

	List<ProcessingNotification> notificationsOf(Repository repository);

}
