package org.kalibro.core.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.kalibro.KalibroException;

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
			throw new KalibroException(noResultMessage, exception);
		}
	}

	protected List<T> getResultList() {
		return nativeQuery.getResultList();
	}

	protected void executeUpdate() {
		nativeQuery.executeUpdate();
	}
}