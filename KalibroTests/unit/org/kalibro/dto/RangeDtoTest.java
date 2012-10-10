package org.kalibro.dto;

import org.kalibro.Range;
import org.kalibro.dao.ReadingDao;

public class RangeDtoTest extends AbstractDtoTest<Range> {

	@Override
	protected Range loadFixture() {
		return loadFixture("lcom4-bad", Range.class);
	}

	@Override
	protected void registerLazyLoadExpectations() {
		whenLazy(ReadingDao.class, "readingOf", entity.getId()).thenReturn(entity.getReading());
	}
}