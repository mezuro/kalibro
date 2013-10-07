package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.RepositoryListener;

/**
 * Data access object for {@link RepositoryListener}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public interface RepositoryListenerDao {

	SortedSet<RepositoryListener> all();

	Long save(RepositoryListener repositoryListener, Long repositoryId);

	void delete(Long repositoryListenerId);

	SortedSet<RepositoryListener> listenersOf(Long repositoryId);
}