package org.kalibro.core.persistence.database;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.EntityManager;

public class DatabaseManager {

	private EntityManager entityManager;

	protected DatabaseManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected <T> Query<T> createQuery(String queryText, Class<T> resultClass) {
		entityManager.clear();
		return new Query<T>(entityManager.createQuery(queryText, resultClass));
	}

	protected void save(Object record) {
		save(record, new NullRunnable());
	}

	protected void save(Object record, Runnable beforeSave) {
		save(Arrays.asList(record), beforeSave);
	}

	protected void save(Collection<?> records) {
		save(records, new NullRunnable());
	}

	protected void save(Collection<?> records, Runnable beforeSave) {
		entityManager.getTransaction().begin();
		beforeSave.run();
		for (Object record : records)
			persist(record);
		entityManager.getTransaction().commit();
	}

	protected void persist(Object record) {
		entityManager.persist(entityManager.merge(record));
	}

	protected void delete(Object record) {
		delete(record, new NullRunnable());
	}

	protected void delete(Object record, Runnable beforeRemove) {
		entityManager.getTransaction().begin();
		beforeRemove.run();
		remove(record);
		entityManager.getTransaction().commit();
	}

	protected void remove(Object record) {
		entityManager.remove(entityManager.merge(record));
	}

	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}
}

class NullRunnable implements Runnable {

	@Override
	public void run() {
		return;
	}
}