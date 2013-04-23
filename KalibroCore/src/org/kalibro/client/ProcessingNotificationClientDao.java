package org.kalibro.client;

import java.util.SortedSet;

import org.kalibro.ProcessingNotification;
import org.kalibro.dao.ProcessingNotificationDao;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.service.ProcessingNotificationEndpoint;
import org.kalibro.service.xml.ProcessingNotificationXml;

public class ProcessingNotificationClientDao extends EndpointClient<ProcessingNotificationEndpoint>
	implements ProcessingNotificationDao {

	public ProcessingNotificationClientDao(String serviceAddress) {
		super(serviceAddress, ProcessingNotificationEndpoint.class);
	}

	@Override
	public SortedSet<ProcessingNotification> all() {
		return DataTransferObject.toSortedSet(port.allProcessingNotifications());
	}

	@Override
	public Long save(ProcessingNotification processingNotification, Long repositoryId) {
		return port.saveProcessingNotification(new ProcessingNotificationXml(processingNotification), repositoryId);
	}

	@Override
	public void delete(Long processingNotificationId) {
		port.deleteProcessingNotification(processingNotificationId);
	}
}