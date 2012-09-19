package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.ReadingGroup;
import org.kalibro.dto.ReadingGroupDto;

/**
 * XML element for {@link ReadingGroup} responses.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "readingGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadingGroupXmlResponse extends ReadingGroupDto {

	@XmlElement
	private Long id;

	@XmlElement
	private String name;

	@XmlElement
	private String description;

	public ReadingGroupXmlResponse() {
		super();
	}

	public ReadingGroupXmlResponse(ReadingGroup group) {
		id = group.getId();
		name = group.getName();
		description = group.getDescription();
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