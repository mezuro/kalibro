package org.kalibro.core.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;

import org.kalibro.core.dto.DataTransferObject;

abstract class DatabaseDao<ENTITY, RECORD extends DataTransferObject<ENTITY>> {

	protected Class<RECORD> recordClass;
	protected RecordManager recordManager;

	protected DatabaseDao(RecordManager recordManager, Class<RECORD> recordClass) {
		this.recordClass = recordClass;
		this.recordManager = recordManager;
	}

	protected List<String> getAllNames() {
		String queryText = "SELECT x.name FROM " + getEntityName() + " x ORDER BY lower(x.name)";
		return recordManager.createQuery(queryText, String.class).getResultList();
	}

	protected boolean hasEntity(String name) {
		String queryText = "SELECT 1 FROM " + getEntityName() + " x WHERE x.name = :name";
		TypedQuery<String> query = recordManager.createQuery(queryText, String.class);
		query.setParameter("name", name);
		return !query.getResultList().isEmpty();
	}

	protected ENTITY getByName(String name) {
		String queryText = "SELECT x FROM " + getEntityName() + " x WHERE x.name = :name";
		TypedQuery<RECORD> query = createRecordQuery(queryText);
		query.setParameter("name", name);
		return query.getSingleResult().convert();
	}

	private String getEntityName() {
		return recordClass.getAnnotation(Entity.class).name();
	}

	protected TypedQuery<RECORD> createRecordQuery(String queryText) {
		return recordManager.createQuery(queryText, recordClass);
	}
}