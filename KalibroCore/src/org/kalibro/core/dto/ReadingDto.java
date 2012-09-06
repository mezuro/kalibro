package org.kalibro.core.dto;

import java.awt.Color;

import org.kalibro.Reading;
import org.kalibro.core.util.DataTransferObject;

public abstract class ReadingDto implements DataTransferObject<Reading> {

	@Override
	public Reading convert() {
		Reading reading = new Reading(label(), grade(), color());
		reading.setId(id());
		return reading;
	}

	protected abstract Long id();

	protected abstract String label();

	protected abstract Double grade();

	protected abstract Color color();
}