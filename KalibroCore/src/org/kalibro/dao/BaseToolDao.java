package org.kalibro.dao;

import java.util.SortedSet;

import org.kalibro.BaseTool;

/**
 * Data access object for {@link BaseTool}.
 * 
 * @author Carlos Morais
 */
public interface BaseToolDao {

	SortedSet<BaseTool> all();
}