package org.kalibro.service.entities;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.model.Range;

@XmlRootElement(name = "Range")
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
		label = range.getLabel();
		grade = range.getGrade();
		color = Integer.toHexString(range.getColor().getRGB());
		comments = range.getComments();
	}

	@Override
	public Range convert() {
		Range range = new Range();
		range.setBeginning(beginning);
		range.setEnd(end);
		range.setLabel(label);
		if (grade != null)
			range.setGrade(grade);
		if (color != null)
			convertColor(range);
		if (comments != null)
			range.setComments(comments);
		return range;
	}

	private void convertColor(Range range) {
		int rgb = (int) Long.parseLong(color, 16);
		range.setColor(new Color(rgb, true));
	}
}