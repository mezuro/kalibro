package org.kalibro.core.persistence;

import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.kalibro.core.Identifier;
import org.kalibro.dto.DataTransferObject;

/**
 * Template for database access objects.
 * 
 * @author Carlos Morais
 */
abstract class DatabaseDao<ENTITY, RECORD extends DataTransferObject<ENTITY>> {

	private RecordManager recordManager;
	private Class<RECORD> recordClass;

	DatabaseDao(RecordManager recordManager, Class<RECORD> recordClass) {
		this.recordManager = recordManager;
		this.recordClass = recordClass;
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
		return recordManager.getById(recordId, recordClass).convert();
	}

	public SortedSet<ENTITY> all() {
		return DataTransferObject.toSortedSet(createRecordQuery("").getResultList());
	}

	protected <T> T save(T record) {
		return recordManager.save(record);
	}

	public void delete(Long recordId) {
		Query query = recordManager.createQuery("DELETE FROM " + entityName() + " WHERE id = :id");
		query.setParameter("id", recordId);
		recordManager.executeUpdate(query);
	}

	protected TypedQuery<RECORD> createRecordQuery(String clauses) {
		String queryString = "SELECT " + alias() + " FROM " + entityName() + " " + alias() + " " + clauses;
		return createQuery(queryString, recordClass);
	}

	protected <T> TypedQuery<T> createQuery(String queryString, Class<T> resultClass) {
		return recordManager.createQuery(queryString, resultClass);
	}

	private String alias() {
		return Identifier.fromVariable(entityName()).asVariable();
	}

	private String entityName() {
		return recordClass.getAnnotation(Entity.class).name();
	}
}