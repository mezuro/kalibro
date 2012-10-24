package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dto.RepositoryDto;

/**
 * XML element for {@link Repository} responses.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "repository")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryXmlResponse extends RepositoryDto {

	@XmlElement
	private Long id;

	@XmlElement
	private String name;

	@XmlElement
	private String description;

	@XmlElement
	private String license;

	@XmlElement
	private Integer processPeriod;

	@XmlElement
	private RepositoryType type;

	@XmlElement
	private String address;

	public RepositoryXmlResponse() {
		super();
	}

	public RepositoryXmlResponse(Repository repository) {
		id = repository.getId();
		name = repository.getName();
		description = repository.getDescription();
		license = repository.getLicense();
		processPeriod = repository.getProcessPeriod();
		type = repository.getType();
		address = repository.getAddress();
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
	public String description() {
		return description;
	}

	@Override
	public String license() {
		return license;
	}

	@Override
	public Integer processPeriod() {
		return processPeriod;
	}

	@Override
	public RepositoryType type() {
		return type;
	}

	@Override
	public String address() {
		return address;
	}
}