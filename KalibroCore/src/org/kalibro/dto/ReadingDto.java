package org.kalibro.dto;

import java.awt.Color;

import org.kalibro.Reading;

/**
 * Data Transfer Object for {@link Reading}.
 * 
 * @author Carlos Morais
 */
public abstract class ReadingDto extends DataTransferObject<Reading> {

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