package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.RepositorySubscriber;

/**
 * Data access object for {@link RepositorySubscriber}.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public interface RepositorySubscriberDao {

	SortedSet<RepositorySubscriber> all();

	Long save(RepositorySubscriber repositorySubscriber, Long repositoryId);

	void delete(Long repositorySubscriberId);

	SortedSet<RepositorySubscriber> subscribersOf(Long repositoryId);
}