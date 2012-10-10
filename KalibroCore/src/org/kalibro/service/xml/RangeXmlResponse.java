package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Range;
import org.kalibro.dto.RangeDto;

/**
 * XML element for {@link Range} responses.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "range")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeXmlResponse extends RangeDto {

	@XmlElement
	private Long id;

	@XmlElement
	private Double beginning;

	@XmlElement
	private Double end;

	@XmlElement
	private String comments;

	public RangeXmlResponse() {
		super();
	}

	public RangeXmlResponse(Range range) {
		id = range.getId();
		beginning = range.getBeginning();
		end = range.getEnd();
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
}