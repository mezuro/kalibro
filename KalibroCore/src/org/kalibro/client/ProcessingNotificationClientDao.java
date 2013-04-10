package org.kalibro.client;

import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.ProcessingNotification;
import org.kalibro.Repository;
import org.kalibro.dao.ProcessingNotificationDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ProcessingNotificationEndpoint;


public class ProcessingNotificationClientDao extends EndpointClient<ProcessingNotificationEndpoint>
	implements ProcessingNotificationDao {

	public ProcessingNotificationClientDao(String serviceAddress) {
		super(serviceAddress, ProcessingNotificationEndpoint.class);
	}

	@Override
	public SortedSet<ProcessingNotification> notificationsOf(Repository repository) {
		SortedSet <ProcessingNotification> allNotifications = 
				DataTransferObject.toSortedSet(port.allProcessingNotifications());
		SortedSet <ProcessingNotification> repositoryNotifications = new TreeSet<ProcessingNotification>();
		for (ProcessingNotification p : allNotifications) {
			if (p.getRepository().getId() == repository.getId())
				repositoryNotifications.add(p);
		}
		return repositoryNotifications;
	}

	@Override
	public SortedSet<ProcessingNotification> all() {
		return DataTransferObject.toSortedSet(port.allProcessingNotifications());
	}

}