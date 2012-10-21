package org.kalibro.core.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import javax.persistence.TypedQuery;

import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.record.ModuleResultRecord;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link ModuleResultDao}.
 * 
 * @author Carlos Morais
 */
public class ModuleResultDatabaseDao extends DatabaseDao<ModuleResult, ModuleResultRecord> implements ModuleResultDao {

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

	public void save(ModuleResult moduleResult, Long processingId) {
		save(new ModuleResultRecord(moduleResult, processingId));
	}

	public ModuleResult prepareResultFor(Module module, Long processingId) {
		ModuleResult moduleResult = findResultFor(module, processingId).convert();
		moduleResult.getModule().setGranularity(module.getGranularity());
		return moduleResult;
	}

	private ModuleResultRecord findResultFor(Module module, Long processingId) {
		List<String> moduleName = Arrays.asList(module.getName());
		if (!exists(moduleNameClause(), "processingId", processingId, "moduleName", moduleName))
			save(new ModuleResultRecord(module, findParentOf(module, processingId), processingId));
		return getResultFor(moduleName, processingId);
	}

	private ModuleResultRecord findParentOf(Module module, Long processingId) {
		if (module.getGranularity() == Granularity.SOFTWARE)
			return null;
		return findResultFor(module.inferParent(), processingId);
	}

	private ModuleResultRecord getResultFor(List<String> moduleName, Long processingId) {
		TypedQuery<ModuleResultRecord> query = createRecordQuery(moduleNameClause());
		query.setParameter("processingId", processingId);
		query.setParameter("moduleName", moduleName);
		return query.getSingleResult();
	}

	private String moduleNameClause() {
		return "WHERE moduleResult.processing.id = :processingId AND moduleResult.moduleName = :moduleName";
	}
}