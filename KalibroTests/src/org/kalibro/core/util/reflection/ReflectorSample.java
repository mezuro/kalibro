package org.kalibro.core.util.reflection;

import javax.persistence.Basic;
import javax.persistence.Id;

class ReflectorSample extends AbstractReflectorSample {

	private static int counter;

	public static int count() {
		return counter;
	}

	@Id
	private int id;

	protected ReflectorSample(String name) {
		super(name);
		this.id = counter;
		setDescription("");
		counter++;
	}

	@Basic
	public int getId() {
		return id;
	}
}