package org.kalibro.service.xml;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.dto.RangeDto;

/**
 * XML element for {@link Range} snapshots.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "range")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeSnapshotXml extends RangeDto {

	@XmlElement
	private Double beginning;

	@XmlElement
	private Double end;

	@XmlElement
	private String label;

	@XmlElement
	private Double grade;

	@XmlElement
	private String color;

	@XmlElement
	private String comments;

	public RangeSnapshotXml() {
		super();
	}

	public RangeSnapshotXml(Range range) {
		beginning = range.getBeginning();
		end = range.getEnd();
		setReading(range.getReading());
		comments = range.getComments();
	}

	private void setReading(Reading reading) {
		if (reading != null) {
			label = reading.getLabel();
			grade = reading.getGrade();
			color = Integer.toHexString(reading.getColor().getRGB()).substring(2);
		}
	}

	@Override
	public Long id() {
		return null;
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
	public Reading reading() {
		return label == null ? null : new Reading(label, grade, new Color(Integer.parseInt(color, 16)));
	}

	@Override
	public String comments() {
		return comments;
	}
}