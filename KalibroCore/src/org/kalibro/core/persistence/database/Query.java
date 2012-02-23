package org.kalibro.core.persistence.database;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

class Query<T> {

	private TypedQuery<T> nativeQuery;

	protected Query(TypedQuery<T> nativeQuery) {
		this.nativeQuery = nativeQuery;
	}

	protected void setParameter(String parameter, Object value) {
		nativeQuery.setParameter(parameter, value);
	}

	protected T getSingleResult() {
		return getSingleResult("No entity found");
	}

	protected T getSingleResult(String noResultMessage) {
		try {
			return nativeQuery.getSingleResult();
		} catch (NoResultException exception) {
			NoResultException modifiedException = new NoResultException(noResultMessage);
			modifiedException.setStackTrace(exception.getStackTrace());
			throw modifiedException;
		}
	}

	protected List<T> getResultList() {
		return nativeQuery.getResultList();
	}

	protected void executeUpdate() {
		nativeQuery.executeUpdate();
	}
}