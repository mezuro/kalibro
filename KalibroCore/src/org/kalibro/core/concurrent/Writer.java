package org.kalibro.core.concurrent;

import org.kalibro.KalibroException;

/**
 * Write objects for a {@link Producer} to yield.
 * 
 * @author Carlos Morais
 */
public class Writer<T> {

	private boolean closed;
	private Producer<T> producer;

	Writer(Producer<T> producer) {
		this.producer = producer;
	}

	public void write(T object) {
		if (closed)
			throw new KalibroException("Writer is closed.");
		producer.write(object);
	}

	public void close() {
		closed = true;
		producer.close(this);
	}
}