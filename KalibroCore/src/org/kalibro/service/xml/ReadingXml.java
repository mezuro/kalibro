package org.kalibro.service.xml;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Reading;
import org.kalibro.dto.ReadingDto;

/**
 * XML element for {@link Reading}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "reading")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadingXml extends ReadingDto {

	@XmlElement
	private Long id;

	@XmlElement(required = true)
	private String label;

	@XmlElement(required = true)
	private Double grade;

	@XmlElement(required = true)
	private String color;

	public ReadingXml() {
		super();
	}

	public ReadingXml(Reading reading) {
		id = reading.getId();
		label = reading.getLabel();
		grade = reading.getGrade();
		color = Integer.toHexString(reading.getColor().getRGB()).substring(2);
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public Double grade() {
		return grade;
	}

	@Override
	public Color color() {
		return new Color(Integer.parseInt(color, 16));
	}
}