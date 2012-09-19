package org.kalibro.core.persistence;

import java.util.List;

import org.kalibro.ReadingGroup;
import org.kalibro.core.persistence.record.ReadingGroupRecord;
import org.kalibro.dao.ReadingGroupDao;

/**
 * Database access implementation for {@link ReadingGroupDao}.
 * 
 * @author Carlos Morais
 */
public class ReadingGroupDatabaseDao extends DatabaseDao<ReadingGroup, ReadingGroupRecord> implements ReadingGroupDao {

	protected ReadingGroupDatabaseDao(RecordManager recordManager) {
		super(recordManager, ReadingGroupRecord.class);
	}

	@Override
	public boolean exists(Long groupId) {
		return existsWithId(groupId);
	}

	@Override
	public ReadingGroup get(Long groupId) {
		return getById(groupId);
	}

	@Override
	public List<ReadingGroup> all() {
		return allOrderedByName();
	}

	@Override
	public Long save(ReadingGroup group) {
		return save(new ReadingGroupRecord(group)).id();
	}

	@Override
	public void delete(Long groupId) {
		deleteById(groupId);
	}
}