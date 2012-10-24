package org.kalibro.core.persistence;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Facilitates interaction with {@link EntityManager}, from the Java Persistence API.
 * 
 * @author Carlos Morais
 */
class RecordManager {

	private EntityManager entityManager;

	RecordManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected <T> T getById(Long id, Class<T> recordClass) {
		return entityManager.find(recordClass, id);
	}

	protected Query createQuery(String queryString) {
		return entityManager.createQuery(queryString);
	}

	protected <T> TypedQuery<T> createQuery(String queryString, Class<T> resultClass) {
		return entityManager.createQuery(queryString, resultClass);
	}

	protected void executeUpdate(Query updateQuery) {
		beginTransaction();
		updateQuery.executeUpdate();
		commitTransaction();
	}

	protected <T> T save(T record) {
		beginTransaction();
		T merged = persist(record);
		commitTransaction();
		return merged;
	}

	protected void saveAll(Collection<?> records) {
		beginTransaction();
		for (Object record : records)
			persist(record);
		commitTransaction();
	}

	private void beginTransaction() {
		entityManager.getTransaction().begin();
	}

	private <T> T persist(T record) {
		T merged = entityManager.merge(record);
		entityManager.persist(merged);
		return merged;
	}

	private void commitTransaction() {
		entityManager.getTransaction().commit();
	}

	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}
}