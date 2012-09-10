package org.kalibro.dto;

import java.util.List;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingDao;

/**
 * Data Transfer Object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public abstract class ReadingGroupDto extends DataTransferObject<ReadingGroup> {

	@Override
	public ReadingGroup convert() {
		ReadingGroup group = new ReadingGroup();
		group.setId(id());
		group.setName(name());
		group.setDescription(description());
		group.setReadings(readings());
		return group;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String description();

	public List<Reading> readings() {
		return (List<Reading>) DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", id());
	}
}