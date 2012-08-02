package org.kalibro.core.persistence.database;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.EntityManager;

class DatabaseManager {

	private EntityManager entityManager;

	protected DatabaseManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected <T> Query<T> createQuery(String queryText, Class<T> resultClass) {
		entityManager.clear();
		return new Query<T>(entityManager.createQuery(queryText, resultClass));
	}

	protected void save(Object record) {
		save(Arrays.asList(record));
	}

	protected void save(Collection<?> records) {
		beginTransaction();
		for (Object record : records)
			persist(record);
		commitTransaction();
	}

	protected void persist(Object record) {
		entityManager.persist(merge(record));
	}

	protected void delete(Object record) {
		beginTransaction();
		remove(record);
		commitTransaction();
	}

	protected void remove(Object record) {
		entityManager.remove(merge(record));
	}

	protected void beginTransaction() {
		entityManager.getTransaction().begin();
	}

	protected void commitTransaction() {
		entityManager.getTransaction().commit();
	}

	protected void evictFromCache(Class<?> classToEvitct) {
		entityManager.getEntityManagerFactory().getCache().evict(classToEvitct);
	}

	private Object merge(Object record) {
		return entityManager.merge(record);
	}

	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}
}