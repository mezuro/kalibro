package org.kalibro.dto;

import java.util.SortedSet;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingDao;

/**
 * Data transfer object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public abstract class ReadingGroupDto extends DataTransferObject<ReadingGroup> {

	@Override
	public ReadingGroup convert() {
		ReadingGroup group = new ReadingGroup();
		setId(group, id());
		group.setName(name());
		group.setDescription(description() == null ? "" : description());
		group.setReadings(readings());
		return group;
	}

	public abstract Long id();

	public abstract String name();

	public abstract String description();

	protected SortedSet<Reading> readings() {
		return DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", id());
	}
}