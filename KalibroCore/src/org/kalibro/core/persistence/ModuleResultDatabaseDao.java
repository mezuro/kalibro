package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.record.ModuleResultRecord;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link ModuleResultDao}.
 * 
 * @author Carlos Morais
 */
class ModuleResultDatabaseDao extends DatabaseDao<ModuleResult, ModuleResultRecord> implements ModuleResultDao {

	ModuleResultDatabaseDao(RecordManager recordManager) {
		super(recordManager, ModuleResultRecord.class);
	}

	@Override
	public ModuleResult resultsRootOf(Long processingId) {
		TypedQuery<ModuleResultRecord> query = createRecordQuery(
			"WHERE moduleResult.processing.id = :processingId AND moduleResult.parent = null");
		query.setParameter("processingId", processingId);
		return query.getSingleResult().convert();
	}

	@Override
	public ModuleResult parentOf(Long moduleResultId) {
		TypedQuery<ModuleResultRecord> query = createRecordQuery(
			"JOIN ModuleResult child ON child.parent = moduleResult WHERE child.id = :childId");
		query.setParameter("childId", moduleResultId);
		return query.getSingleResult().convert();
	}

	@Override
	public SortedSet<ModuleResult> childrenOf(Long moduleResultId) {
		TypedQuery<ModuleResultRecord> query = createRecordQuery(
			"JOIN ModuleResult parent ON moduleResult.parent = parent WHERE parent.id = :parentId");
		query.setParameter("parentId", moduleResultId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}
}