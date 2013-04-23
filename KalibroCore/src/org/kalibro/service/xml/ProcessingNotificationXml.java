package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.ProcessingNotification;
import org.kalibro.dto.ProcessingNotificationDto;

@XmlRootElement(name = "processingNotification")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessingNotificationXml extends ProcessingNotificationDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement(required = true)
	private String email;

	public ProcessingNotificationXml() {
		super();
	}

	public ProcessingNotificationXml(ProcessingNotification processingNotification) {
		id = processingNotification.getId();
		name = processingNotification.getName();
		email = processingNotification.getEmail();
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String email() {
		return email;
	}
}
