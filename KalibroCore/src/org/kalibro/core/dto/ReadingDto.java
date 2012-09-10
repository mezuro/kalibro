package org.kalibro.core.dto;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.Reading;

/**
 * Data Transfer Object for {@link Reading}.
 * 
 * @author Carlos Morais
 */
public abstract class ReadingDto implements DataTransferObject<Reading> {

	public static List<Reading> convert(Collection<? extends ReadingDto> readings) {
		List<Reading> converted = new ArrayList<Reading>();
		for (ReadingDto reading : readings)
			converted.add(reading.convert());
		return converted;
	}

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