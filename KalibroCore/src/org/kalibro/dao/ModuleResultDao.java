package org.kalibro.dao;

import java.util.Date;
import java.util.SortedMap;
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

	SortedMap<Date, ModuleResult> historyOf(Long moduleResultId);
}