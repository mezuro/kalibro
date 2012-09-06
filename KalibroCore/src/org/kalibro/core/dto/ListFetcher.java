package org.kalibro.core.dto;

import java.util.List;

/**
 * Abstract Fetcher for lists.
 * 
 * @author Carlos Morais
 */
@SuppressWarnings("rawtypes")
abstract class ListFetcher<T> extends Fetcher<List> {

	protected List<T> createProxy() {
		return super.createProxy(List.class);
	}
}