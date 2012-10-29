package org.kalibro.core.persistence;

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

	Query createQuery(String queryString) {
		return entityManager.createQuery(queryString);
	}

	<T> TypedQuery<T> createQuery(String queryString, Class<T> resultClass) {
		return entityManager.createQuery(queryString, resultClass);
	}

	<T> T getById(Long id, Class<T> recordClass) {
		return entityManager.find(recordClass, id);
	}

	<T> T save(T record) {
		beginTransaction();
		T merged = entityManager.merge(record);
		entityManager.persist(merged);
		commitTransaction();
		return merged;
	}

	void removeById(Long id, Class<?> recordClass) {
		beginTransaction();
		entityManager.remove(getById(id, recordClass));
		commitTransaction();
	}

	private void beginTransaction() {
		entityManager.getTransaction().begin();
	}

	private void commitTransaction() {
		entityManager.getTransaction().commit();
	}
}