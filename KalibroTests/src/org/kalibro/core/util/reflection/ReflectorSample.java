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

	protected ReflectorSample() {
		this("");
	}

	protected ReflectorSample(String name) {
		setId(counter);
		setName(name);
		setDescription("");
		counter++;
	}

	@Basic
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}