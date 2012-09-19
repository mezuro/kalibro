package org.kalibro.core.abstractentity;

import java.util.ArrayList;
import java.util.List;

class Programmer extends Person {

	@SuppressWarnings("unused")
	private Boolean useMetrics;

	@SuppressWarnings("unused")
	private List<Programmer> colleagues;

	protected Programmer() {
		this(new Person(), false);
	}

	protected Programmer(Person person, Boolean useMetrics) {
		super(person.getIdentityNumber(), person.getName(), person.getSex());
		this.useMetrics = useMetrics;
		colleagues = new ArrayList<Programmer>();
	}
}