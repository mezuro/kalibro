package org.kalibro.core.concurrent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.kalibro.KalibroException;

/**
 * Produces objects to be consumed concurrently. The production is done by creating {@link Writer}s. The consumption is
 * done by just iterating at the producer. The iteration ends when all objects written were already consumed and all
 * writers created by this producer were closed.
 * 
 * @author Carlos Morais
 */
public class Producer<T> implements Iterable<T> {

	private boolean started;
	private BlockingQueue<T> queue;
	private Set<Writer<T>> openWriters;

	public Producer() {
		queue = new LinkedBlockingQueue<T>();
		openWriters = new HashSet<Writer<T>>();
	}

	public Writer<T> createWriter() {
		Writer<T> writer = new Writer<T>(this);
		openWriters.add(writer);
		return writer;
	}

	void write(T product) {
		queue.add(product);
		notifyEvent();
	}

	void close(Writer<T> writer) {
		openWriters.remove(writer);
		notifyEvent();
	}

	synchronized boolean canYield() {
		if (!queue.isEmpty())
			return true;
		if (openWriters.isEmpty())
			return false;
		waitEvent();
		return canYield();
	}

	T yield() {
		try {
			return queue.take();
		} catch (InterruptedException interruption) {
			throw exception(interruption);
		}
	}

	@Override
	public Iterator<T> iterator() {
		while (!started)
			waitEvent();
		return new ProducerIterator<T>(this);
	}

	private synchronized void notifyEvent() {
		started = true;
		notify();
	}

	private synchronized void waitEvent() {
		try {
			wait();
		} catch (InterruptedException interruption) {
			throw exception(interruption);
		}
	}

	private KalibroException exception(InterruptedException interruption) {
		return new KalibroException("Producer interrupted while yielding.", interruption);
	}
}