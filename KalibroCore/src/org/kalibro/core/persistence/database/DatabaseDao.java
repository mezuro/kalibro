package org.kalibro.core.persistence.database;

import java.util.List;

import javax.persistence.Entity;

import org.kalibro.core.util.DataTransferObject;
import org.kalibro.core.util.Identifier;

abstract class DatabaseDao<ENTITY, RECORD extends DataTransferObject<ENTITY>> {

	protected Class<RECORD> recordClass;
	protected DatabaseManager databaseManager;

	protected DatabaseDao(DatabaseManager databaseManager, Class<RECORD> recordClass) {
		this.recordClass = recordClass;
		this.databaseManager = databaseManager;
	}

	protected List<String> getAllNames() {
		String queryText = "SELECT x.name FROM " + getEntityName() + " x ORDER BY x.name";
		return databaseManager.createQuery(queryText, String.class).getResultList();
	}

	protected boolean hasEntity(String name) {
		String queryText = "SELECT 1 FROM " + getEntityName() + " x WHERE x.name = :name";
		Query<String> query = databaseManager.createQuery(queryText, String.class);
		query.setParameter("name", name);
		return ! query.getResultList().isEmpty();
	}

	protected ENTITY getByName(String name) {
		String queryText = "SELECT x FROM " + getEntityName() + " x WHERE x.name = :name";
		Query<RECORD> query = createRecordQuery(queryText);
		query.setParameter("name", name);
		return query.getSingleResult(noResultMessage(name)).convert();
	}

	private String noResultMessage(String name) {
		String entityName = Identifier.fromVariable(getEntityName()).asText().toLowerCase();
		entityName = entityName.replace('\"', ' ').trim();
		return "There is no " + entityName + " named '" + name + "'";
	}

	private String getEntityName() {
		return recordClass.getAnnotation(Entity.class).name();
	}

	protected Query<RECORD> createRecordQuery(String queryText) {
		return databaseManager.createQuery(queryText, recordClass);
	}
}