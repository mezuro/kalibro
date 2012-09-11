package org.kalibro.core.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

	protected Query createQuery(String queryText) {
		return entityManager.createQuery(queryText);
	}

	protected <T> TypedQuery<T> createQuery(String queryText, Class<T> resultClass) {
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

	private <T> T persist(T record) {
		T merged = entityManager.merge(record);
		entityManager.persist(merged);
		return merged;
	}

	@Deprecated
	protected void remove(Object record) {
		entityManager.remove(entityManager.merge(record));
	}

	protected void beginTransaction() {
		entityManager.getTransaction().begin();
	}

	protected void commitTransaction() {
		entityManager.getTransaction().commit();
	}

	@Deprecated
	protected void evictFromCache(Class<?> classToEvict) {
		entityManager.getEntityManagerFactory().getCache().evict(classToEvict);
	}

	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}
}