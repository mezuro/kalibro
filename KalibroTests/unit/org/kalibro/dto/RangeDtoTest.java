package org.kalibro.dto;

import java.util.List;

import org.kalibro.Range;
import org.kalibro.dao.ReadingDao;

public class RangeDtoTest extends AbstractDtoTest<Range> {

	@Override
	protected Range loadFixture() {
		return loadFixture("lcom4-bad", Range.class);
	}

	@Override
	protected List<LazyLoadExpectation> lazyLoadExpectations() {
		return asList(expectLazy(entity.getReading(), ReadingDao.class, "readingOf", entity.getId()));
	}
}