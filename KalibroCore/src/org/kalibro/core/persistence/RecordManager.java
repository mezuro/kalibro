package org.kalibro.core.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
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

	protected <T> TypedQuery<T> createQuery(String queryText, Class<T> resultClass) {
		entityManager.clear();
		return entityManager.createQuery(queryText, resultClass);
	}

	protected <T> T save(T record) {
		return save(Arrays.asList(record)).get(0);
	}

	protected <T> List<T> save(Collection<T> records) {
		List<T> merged = new ArrayList<T>();
		beginTransaction();
		for (T record : records)
			merged.add(persist(record));
		commitTransaction();
		return merged;
	}

	protected <T> T persist(T record) {
		T merged = merge(record);
		entityManager.persist(merged);
		return merged;
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

	private <T> T merge(T record) {
		return entityManager.merge(record);
	}

	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}
}