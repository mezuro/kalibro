package org.kalibro.core.dto;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

/**
 * Creates lazy load proxies.
 * 
 * @author Carlos Morais
 */
final class ProxyFactory {

	public static <T> T lazyLoadProxy(Class<T> targetClass, final Loader<? extends T> loader) {
		return (T) Enhancer.create(targetClass, new LazyLoader() {

			@Override
			public Object loadObject() throws Exception {
				return loader.load();
			}
		});
	}

	private ProxyFactory() {
		return;
	}
}