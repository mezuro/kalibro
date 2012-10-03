package org.kalibro.service.xml;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Range;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "range")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeXml extends DataTransferObject<Range> {

	@XmlElement(required = true)
	private Double beginning;

	@XmlElement(required = true)
	private Double end;

	@XmlElement(required = true)
	private String label;

	@XmlElement(required = true)
	private Double grade;

	private String color;
	private String comments;

	public RangeXml() {
		super();
	}

	public RangeXml(Range range) {
		beginning = range.getBeginning();
		end = range.getEnd();
		label = range.getReading().getLabel();
		grade = range.getReading().getGrade();
		color = Integer.toHexString(range.getReading().getColor().getRGB());
		comments = range.getComments();
	}

	@Override
	public Range convert() {
		Range range = new Range(beginning, end);
		convertColor(range);
		return range;
	}

	private void convertColor(Range range) {
		int rgb = (int) Long.parseLong(color, 16);
		range.getReading().setColor(new Color(rgb, true));
	}
}