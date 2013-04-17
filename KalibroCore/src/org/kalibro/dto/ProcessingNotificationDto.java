package org.kalibro.dto;

import org.kalibro.ProcessingNotification;

public abstract class ProcessingNotificationDto extends DataTransferObject<ProcessingNotification> {

	@Override
	public ProcessingNotification convert() {
		ProcessingNotification notification = new ProcessingNotification();
		setId(notification, id());
		notification.setName(name());
		notification.setEmail(email());
		return notification;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String email();
}