package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.RepositoryObserver;
import org.kalibro.dto.RepositoryObserverDto;

/**
 * XML element for {@link RepositoryObserver}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@XmlRootElement(name = "repositoryObserver")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryObserverXml extends RepositoryObserverDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement(required = true)
	private String email;

	public RepositoryObserverXml() {
		super();
	}

	public RepositoryObserverXml(RepositoryObserver repositoryObserver) {
		id = repositoryObserver.getId();
		name = repositoryObserver.getName();
		email = repositoryObserver.getEmail();
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
