package org.kalibro.core.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.util.Identifier;

abstract class DatabaseDao<ENTITY, RECORD extends DataTransferObject<ENTITY>> {

	protected Class<RECORD> recordClass;
	protected RecordManager recordManager;

	protected DatabaseDao(RecordManager recordManager, Class<RECORD> recordClass) {
		this.recordClass = recordClass;
		this.recordManager = recordManager;
	}

	protected List<ENTITY> allOrderedByName() {
		String queryString = "SELECT $x FROM " + getEntityName() + " $x ORDER BY lower($x.name)";
		queryString = queryString.replace("$x", getVariableName());
		List<ENTITY> all = new ArrayList<ENTITY>();
		for (RECORD record : createRecordQuery(queryString).getResultList())
			all.add(record.convert());
		return all;
	}

	protected void deleteById(Long recordId) {
		recordManager.beginTransaction();
		Query query = recordManager.createQuery("DELETE FROM " + getEntityName() + " WHERE id = :id");
		query.setParameter("id", recordId);
		query.executeUpdate();
		recordManager.commitTransaction();
	}

	@Deprecated
	protected List<String> getAllNames() {
		String queryText = "SELECT x.name FROM " + getEntityName() + " x ORDER BY lower(x.name)";
		return recordManager.createQuery(queryText, String.class).getResultList();
	}

	@Deprecated
	protected boolean hasEntity(String name) {
		String queryText = "SELECT 1 FROM " + getEntityName() + " x WHERE x.name = :name";
		TypedQuery<String> query = recordManager.createQuery(queryText, String.class);
		query.setParameter("name", name);
		return !query.getResultList().isEmpty();
	}

	@Deprecated
	protected ENTITY getByName(String name) {
		String queryText = "SELECT x FROM " + getEntityName() + " x WHERE x.name = :name";
		TypedQuery<RECORD> query = createRecordQuery(queryText);
		query.setParameter("name", name);
		return query.getSingleResult().convert();
	}

	private String getVariableName() {
		return Identifier.fromVariable(getEntityName()).asVariable();
	}

	private String getEntityName() {
		return recordClass.getAnnotation(Entity.class).name();
	}

	protected TypedQuery<RECORD> createRecordQuery(String queryString) {
		return recordManager.createQuery(queryString, recordClass);
	}
}