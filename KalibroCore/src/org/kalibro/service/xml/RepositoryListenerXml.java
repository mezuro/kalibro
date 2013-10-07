package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.RepositoryListener;
import org.kalibro.dto.RepositoryListenerDto;

/**
 * XML element for {@link RepositoryListener}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@XmlRootElement(name = "repositoryListener")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryListenerXml extends RepositoryListenerDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement(required = true)
	private String email;

	public RepositoryListenerXml() {
		super();
	}

	public RepositoryListenerXml(RepositoryListener repositoryListener) {
		id = repositoryListener.getId();
		name = repositoryListener.getName();
		email = repositoryListener.getEmail();
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
