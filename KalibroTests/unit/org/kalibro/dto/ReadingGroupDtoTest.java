package org.kalibro.dto;

import java.util.Arrays;
import java.util.List;

import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingDao;

public class ReadingGroupDtoTest extends AbstractDtoTest<ReadingGroup> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("scholar", ReadingGroup.class);
	}

	@Override
	protected List<LazyLoadExpectation> lazyLoadExpectations() {
		return Arrays.asList(expectLazy(entity.getReadings(), ReadingDao.class, "readingsOf", entity.getId()));
	}
}