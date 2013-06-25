package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.RepositoryObserver;

public interface RepositoryObserverDao {

	SortedSet<RepositoryObserver> all();

	Long save(RepositoryObserver repositoryObserver, Long repositoryId);

	void delete(Long repositoryObserverId);
}