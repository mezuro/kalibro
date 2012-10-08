package org.kalibro.dto;

import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;

public abstract class RangeDto extends DataTransferObject<Range> {

	@Override
	public Range convert() {
		Range range = new Range(beginning(), end());
		setId(range, id());
		range.setComments(comments());
		range.setReading(reading());
		return range;
	}

	public abstract Long id();

	public abstract Double beginning();

	public abstract Double end();

	public abstract String comments();

	public Reading reading() {
		return DaoLazyLoader.createProxy(ReadingDao.class, "readingOf", id());
	}
}