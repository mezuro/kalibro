package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.RepositoryType;

public interface RepositoryDao {

	SortedSet<RepositoryType> supportedTypes();
}