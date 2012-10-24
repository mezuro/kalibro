package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;

/**
 * Data access object for {@link Repository}.
 * 
 * @author Carlos Morais
 */
public interface RepositoryDao {

	SortedSet<RepositoryType> supportedTypes();

	Repository repositoryOf(Long processingId);

	SortedSet<Repository> repositoriesOf(Long projectId);

	Long save(Repository repository, Long projectId);

	void process(Long repositoryId);

	void cancelProcessing(Long repositoryId);

	void delete(Long repositoryId);
}