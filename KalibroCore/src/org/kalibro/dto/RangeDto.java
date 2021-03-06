package org.kalibro.dto;

import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;

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
		range.setReading(readingId() == null ? null : reading());
		range.setComments(comments() == null ? "" : comments());
		return range;
	}

	public abstract Long id();

	public abstract Double beginning();

	public abstract Double end();

	protected Reading reading() {
		return DaoLazyLoader.createProxy(ReadingDao.class, "get", readingId());
	}

	public abstract Long readingId();

	public abstract String comments();
}