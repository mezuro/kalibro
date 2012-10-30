package org.kalibro.dto;

import org.kalibro.Range;
import org.kalibro.Reading;

/**
 * Data transfer object for {@link Range}.
 * 
 * @author Carlos Morais
 */
public abstract class RangeDto extends DataTransferObject<Range> {

	@Override
	public Range convert() {
		Range range = new Range(beginning(), end());
		setId(range, id());
		range.setReading(reading());
		range.setComments(comments() == null ? "" : comments());
		return range;
	}

	public abstract Long id();

	public abstract Double beginning();

	public abstract Double end();

	public abstract Reading reading();

	public abstract String comments();
}