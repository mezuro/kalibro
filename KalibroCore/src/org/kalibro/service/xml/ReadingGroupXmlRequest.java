package org.kalibro.service.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.dto.ReadingGroupDto;

/**
 * XML element for {@link ReadingGroup} requests.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "readingGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadingGroupXmlRequest extends ReadingGroupDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String name;

	@XmlElement
	private String description;

	@XmlElement(name = "reading")
	private Collection<ReadingXml> readings;

	public ReadingGroupXmlRequest() {
		super();
	}

	public ReadingGroupXmlRequest(ReadingGroup group) {
		id = group.getId();
		name = group.getName();
		description = group.getDescription();
		setReadings(group.getReadings());
	}

	private void setReadings(List<Reading> readings) {
		this.readings = new ArrayList<ReadingXml>();
		for (Reading reading : readings)
			this.readings.add(new ReadingXml(reading));
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
	public List<Reading> readings() {
		return readings == null ? new ArrayList<Reading>() : toList(readings);
	}
}