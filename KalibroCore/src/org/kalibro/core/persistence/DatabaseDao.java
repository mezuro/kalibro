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
		Query query = recordManager.createQuery("SELECT 1 FROM " + entityName() + " WHERE id = :id");
		query.setParameter("id", recordId);
		return !query.getResultList().isEmpty();
	}

	public ENTITY get(Long recordId) {
		return recordManager.getById(recordId, recordClass).convert();
	}

	public SortedSet<ENTITY> all() {
		return DataTransferObject.toSortedSet(createRecordQuery("").getResultList());
	}

	protected RECORD save(RECORD record) {
		return recordManager.save(record);
	}

	public void delete(Long recordId) {
		Query query = recordManager.createQuery("DELETE FROM " + entityName() + " WHERE id = :id");
		query.setParameter("id", recordId);
		recordManager.executeUpdate(query);
	}

	protected TypedQuery<RECORD> createRecordQuery(String clauses) {
		String queryString = "SELECT " + alias() + " FROM " + entityName() + " " + alias() + " " + clauses;
		return recordManager.createQuery(queryString, recordClass);
	}

	private String alias() {
		return Identifier.fromVariable(entityName()).asVariable();
	}

	private String entityName() {
		return recordClass.getAnnotation(Entity.class).name();
	}
}