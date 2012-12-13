package org.kalibro.service.xml;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Configuration;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.dto.RepositoryDto;

/**
 * XML element for {@link Repository}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "repository")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryXml extends RepositoryDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement
	private String description;

	@XmlElement
	private String license;

	@XmlElement
	private Integer processPeriod;

	@XmlElement(required = true)
	private RepositoryType type;

	@XmlElement(required = true)
	private String address;

	@XmlElement(required = true)
	private Long configurationId;

	@XmlElement
	private Collection<String> mailsToNotify;

	public RepositoryXml() {
		super();
	}

	public RepositoryXml(Repository repository) {
		id = repository.getId();
		name = repository.getName();
		description = repository.getDescription();
		license = repository.getLicense();
		processPeriod = repository.getProcessPeriod();
		type = repository.getType();
		address = repository.getAddress();
		configurationId = repository.getConfiguration().getId();
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

	@Override
	public Configuration configuration() {
		return DaoLazyLoader.createProxy(ConfigurationDao.class, "get", configurationId);
	}
}