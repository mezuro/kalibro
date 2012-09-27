package org.kalibro.core.persistence;

import java.util.List;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.kalibro.core.Identifier;
import org.kalibro.dto.DataTransferObject;

/**
 * Abstract database access implementation for data access objects.
 * 
 * @author Carlos Morais
 */
abstract class DatabaseDao<ENTITY, RECORD extends DataTransferObject<ENTITY>> {

	private RecordManager recordManager;
	private Class<RECORD> recordClass;

	protected DatabaseDao(RecordManager recordManager, Class<RECORD> recordClass) {
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

	@Deprecated
	protected RecordManager recordManager() {
		return recordManager;
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
}