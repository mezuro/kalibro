package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.Project;

/**
 * Data access object for {@link Project}.
 * 
 * @author Carlos Morais
 */
public interface ProjectDao {

	boolean exists(Long projectId);

	Project get(Long projectId);

	SortedSet<Project> all();

	Long save(Project project);

	void delete(Long projectId);
}