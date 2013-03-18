package org.kalibro.core.persistence;

import java.util.Collection;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.kalibro.KalibroException;
import org.kalibro.core.Identifier;
import org.kalibro.dto.DataTransferObject;

/**
 * Template for database access objects.
 * 
 * @author Carlos Morais
 */
abstract class DatabaseDao<ENTITY, RECORD extends DataTransferObject<ENTITY>> {

	private Class<RECORD> recordClass;
	private RecordManager recordManager;

	DatabaseDao(Class<RECORD> recordClass) {
		this.recordClass = recordClass;
		this.recordManager = DatabaseDaoFactory.createRecordManager();
	}

	public boolean exists(Long recordId) {
		return exists("WHERE " + alias() + ".id = :id", "id", recordId);
	}

	protected boolean exists(String clauses, Object... parameters) {
		Query query = recordManager.createQuery("SELECT 1 FROM " + entityName() + " " + alias() + " " + clauses);
		for (int i = 0; i < parameters.length; i += 2)
			query.setParameter(parameters[i].toString(), parameters[i + 1]);
		return !query.getResultList().isEmpty();
	}

	public ENTITY get(Long recordId) {
		RECORD record = recordManager.getById(recordId, recordClass);
		if (record == null)
			throw new KalibroException(entityName() + " " + recordId + " not found.");
		return record.convert();
	}

	public SortedSet<ENTITY> all() {
		return DataTransferObject.toSortedSet(createRecordQuery(null).getResultList());
	}

	protected TypedQuery<RECORD> createRecordQuery(String where) {
		String queryString = "SELECT " + alias() + " FROM " + entityName() + " " + alias();
		if (where != null)
			queryString += " WHERE " + where;
		return createQuery(queryString, recordClass);
	}

	protected <T> TypedQuery<T> createQuery(String queryString, Class<T> resultClass) {
		return recordManager.createQuery(queryString, resultClass);
	}

	private String alias() {
		return Identifier.fromVariable(entityName()).asVariable();
	}

	protected <T> T save(T record) {
		return recordManager.save(record);
	}

	protected <T> void saveAll(Collection<T> records) {
		recordManager.saveAll(records);
	}

	public void delete(Long recordId) {
		recordManager.removeById(recordId, recordClass);
	}

	private String entityName() {
		return recordClass.getAnnotation(Entity.class).name();
	}
}