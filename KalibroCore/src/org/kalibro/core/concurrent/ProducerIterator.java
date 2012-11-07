package org.kalibro.core.concurrent;

import java.util.Iterator;

/**
 * Iterator for {@link Producer}.
 * 
 * @author Carlos Morais
 */
class ProducerIterator<T> implements Iterator<T> {

	private Producer<T> producer;

	ProducerIterator(Producer<T> producer) {
		this.producer = producer;
	}

	@Override
	public boolean hasNext() {
		return producer.canYield();
	}

	@Override
	public T next() {
		return producer.yield();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Cannot remove from producer.");
	}
}