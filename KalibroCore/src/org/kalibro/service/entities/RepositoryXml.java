package org.kalibro.service.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "Repository")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryXml extends DataTransferObject<Repository> {

	@XmlElement(required = true)
	private RepositoryType type;

	@XmlElement(required = true)
	private String address;

	private String username;
	private String password;

	public RepositoryXml() {
		super();
	}

	public RepositoryXml(Repository repository) {
		type = repository.getType();
		address = repository.getAddress();
		username = repository.getUsername();
		password = repository.getPassword();
	}

	@Override
	public Repository convert() {
		Repository repository = new Repository(type, address);
		if (username != null)
			repository.setUsername(username);
		if (password != null)
			repository.setPassword(password);
		return repository;
	}
}