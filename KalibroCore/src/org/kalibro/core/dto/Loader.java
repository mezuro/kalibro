package org.kalibro.core.dto;

/**
 * Loader for creating lazy loading proxies. See {@link ProxyFactory}
 * 
 * @author Carlos Morais
 */
public interface Loader<T> {

	T load();
}