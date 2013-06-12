package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.ProcessingObserver;
import org.kalibro.dto.ProcessingObserverDto;

@XmlRootElement(name = "processingObserver")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessingObserverXml extends ProcessingObserverDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement(required = true)
	private String email;

	public ProcessingObserverXml() {
		super();
	}

	public ProcessingObserverXml(ProcessingObserver processingObserver) {
		id = processingObserver.getId();
		name = processingObserver.getName();
		email = processingObserver.getEmail();
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
