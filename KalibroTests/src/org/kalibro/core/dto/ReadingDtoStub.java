package org.kalibro.core.dto;

import java.awt.Color;

import org.kalibro.Reading;

class ReadingDtoStub extends ReadingDto {

	private Reading reading;

	ReadingDtoStub(Reading reading) {
		this.reading = reading;
	}

	@Override
	protected Long id() {
		return reading.getId();
	}

	@Override
	protected String label() {
		return reading.getLabel();
	}

	@Override
	protected Double grade() {
		return reading.getGrade();
	}

	@Override
	protected Color color() {
		return reading.getColor();
	}
}