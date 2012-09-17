package org.kalibro.core.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.kalibro.dto.DataTransferObject;
import org.kalibro.util.Identifier;

/**
 * Abstract database access implementation for data access objects.
 * 
 * @author Carlos Morais
 */
abstract class DatabaseDao<ENTITY, RECORD extends DataTransferObject<ENTITY>> {

	private Class<RECORD> recordClass;

	@Deprecated
	protected RecordManager recordManager;

	protected DatabaseDao(RecordManager recordManager, Class<RECORD> recordClass) {
		this.recordClass = recordClass;
		this.recordManager = recordManager;
	}

	protected boolean existsWithId(Long recordId) {
		Query query = recordManager.createQuery("SELECT 1 FROM " + entityName() + " WHERE id = :id");
		query.setParameter("id", recordId);
		return !query.getResultList().isEmpty();
	}

	protected ENTITY getById(Long recordId) {
		return recordManager.getById(recordId, recordClass).convert();
	}

	protected List<ENTITY> allOrderedByName() {
		TypedQuery<RECORD> query = createRecordQuery("ORDER BY lower(" + alias() + ".name)");
		return DataTransferObject.convert(query.getResultList());
	}

	protected RECORD save(RECORD record) {
		return recordManager.save(record);
	}

	protected void deleteById(Long recordId) {
		Query query = recordManager.createQuery("DELETE FROM " + entityName() + " WHERE id = :id");
		query.setParameter("id", recordId);
		recordManager.executeUpdate(query);
	}

	@Deprecated
	protected List<String> getAllNames() {
		String queryText = "SELECT x.name FROM " + entityName() + " x ORDER BY lower(x.name)";
		return recordManager.createQuery(queryText, String.class).getResultList();
	}

	@Deprecated
	protected boolean hasEntity(String name) {
		String queryText = "SELECT 1 FROM " + entityName() + " x WHERE x.name = :name";
		TypedQuery<String> query = recordManager.createQuery(queryText, String.class);
		query.setParameter("name", name);
		return !query.getResultList().isEmpty();
	}

	@Deprecated
	protected ENTITY getByName(String name) {
		TypedQuery<RECORD> query = createRecordQuery("WHERE " + alias() + ".name = :name");
		query.setParameter("name", name);
		return query.getSingleResult().convert();
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