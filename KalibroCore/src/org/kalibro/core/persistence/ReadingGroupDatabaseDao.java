package org.kalibro.core.persistence;

import org.kalibro.ReadingGroup;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.kalibro.dao.ReadingGroupDao;

/**
 * Database access implementation for {@link ReadingGroupDao}.
 * 
 * @author Carlos Morais
 */
class ReadingGroupDatabaseDao extends DatabaseDao<ReadingGroup, ReadingGroupRecord> implements ReadingGroupDao {

	ReadingGroupDatabaseDao() {
		super(ReadingGroupRecord.class);
	}

	@Override
	public Long save(ReadingGroup group) {
		return save(new ReadingGroupRecord(group)).id();
	}
}