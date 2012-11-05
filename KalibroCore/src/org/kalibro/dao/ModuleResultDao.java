package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.ModuleResult;

/**
 * Data access object for {@link ModuleResult}.
 * 
 * @author Carlos Morais
 */
public interface ModuleResultDao {

	ModuleResult get(Long moduleResultId);

	SortedSet<ModuleResult> childrenOf(Long moduleResultId);
}