package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Configuration;
import org.kalibro.dto.ConfigurationDto;

/**
 * XML element for {@link Configuration}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationXml extends ConfigurationDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement
	private String description;

	public ConfigurationXml() {
		super();
	}

	public ConfigurationXml(Configuration configuration) {
		id = configuration.getId();
		name = configuration.getName();
		description = configuration.getDescription();
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
}