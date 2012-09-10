package org.kalibro.dto;

import java.awt.Color;

import org.kalibro.Reading;

class ReadingDtoStub extends ReadingDto {

	private Reading reading;

	ReadingDtoStub(Reading reading) {
		this.reading = reading;
	}

	@Override
	public Long id() {
		return reading.getId();
	}

	@Override
	public String label() {
		return reading.getLabel();
	}

	@Override
	public Double grade() {
		return reading.getGrade();
	}

	@Override
	public Color color() {
		return reading.getColor();
	}
}