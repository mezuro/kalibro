package org.kalibro.core.dto;

import java.util.List;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.core.dao.ReadingDao;

public abstract class ReadingGroupDto implements DataTransferObject<ReadingGroup> {

	@Override
	public ReadingGroup convert() {
		ReadingGroup group = new ReadingGroup();
		group.setId(id());
		group.setName(name());
		group.setDescription(description());
		group.setReadings(readings());
		return group;
	}

	protected abstract Long id();

	protected abstract String name();

	protected abstract String description();

	private List<Reading> readings() {
		return (List<Reading>) DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", id());
	}
}