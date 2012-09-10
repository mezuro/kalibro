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

	public abstract Long id();

	public abstract String label();

	public abstract Double grade();

	public abstract Color color();
}