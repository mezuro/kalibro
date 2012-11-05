package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Range;
import org.kalibro.dto.RangeDto;

/**
 * XML element for {@link Range}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "range")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeXml extends RangeDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private Double beginning;

	@XmlElement(required = true)
	private Double end;

	@XmlElement
	private Long readingId;

	@XmlElement
	private String comments;

	public RangeXml() {
		super();
	}

	public RangeXml(Range range) {
		id = range.getId();
		beginning = range.getBeginning();
		end = range.getEnd();
		readingId = range.hasReading() ? range.getReading().getId() : null;
		comments = range.getComments();
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public Double beginning() {
		return beginning;
	}

	@Override
	public Double end() {
		return end;
	}

	@Override
	public String comments() {
		return comments;
	}

	@Override
	public Long readingId() {
		return readingId;
	}
}