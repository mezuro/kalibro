package org.kalibro.dao;

import java.util.Set;
import java.util.SortedSet;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;

public interface RepositoryDao {

	SortedSet<RepositoryType> supportedTypes();

	Long save(Repository repository, Long projectId);

	void process(Long repositoryId);

	void cancelProcessing(Long repositoryId);

	void delete(Long repositoryId);

	Set<Repository> repositoriesOf(Long projectId);
}