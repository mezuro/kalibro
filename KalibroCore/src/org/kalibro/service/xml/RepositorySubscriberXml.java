package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.RepositorySubscriber;
import org.kalibro.dto.RepositorySubscriberDto;

/**
 * XML element for {@link RepositorySubscriber}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@XmlRootElement(name = "repositorySubscriber")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositorySubscriberXml extends RepositorySubscriberDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement(required = true)
	private String email;

	public RepositorySubscriberXml() {
		super();
	}

	public RepositorySubscriberXml(RepositorySubscriber repositorySubscriber) {
		id = repositorySubscriber.getId();
		name = repositorySubscriber.getName();
		email = repositorySubscriber.getEmail();
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
