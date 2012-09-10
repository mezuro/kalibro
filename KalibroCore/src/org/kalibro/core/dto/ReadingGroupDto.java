package org.kalibro.core.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.core.dao.ReadingDao;

/**
 * Data Transfer Object for {@link ReadingGroup}.
 * 
 * @author Carlos Morais
 */
public abstract class ReadingGroupDto implements DataTransferObject<ReadingGroup> {

	public static List<ReadingGroup> convert(Collection<? extends ReadingGroupDto> groups) {
		List<ReadingGroup> converted = new ArrayList<ReadingGroup>();
		for (ReadingGroupDto group : groups)
			converted.add(group.convert());
		return converted;
	}

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

	protected List<Reading> readings() {
		return (List<Reading>) DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", id());
	}
}