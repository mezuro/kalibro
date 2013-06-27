package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.RepositoryObserver;

/**
 * Data access object for {@link RepositoryObserver}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public interface RepositoryObserverDao {

	SortedSet<RepositoryObserver> all();

	Long save(RepositoryObserver repositoryObserver, Long repositoryId);

	void delete(Long repositoryObserverId);

	SortedSet<RepositoryObserver> observersOf(Long repositoryId);
}