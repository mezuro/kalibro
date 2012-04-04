package org.kalibro.core.model.abstracts;

final class PersonFixtures {

	protected static Person carlos() {
		Person carlos = new Person("CM", "Carlos Morais", "Male");
		carlos.addRelative("mother", cristina());
		carlos.addRelative("sister", isis());
		return carlos;
	}

	protected static Person cristina() {
		return new Person("CN", "Cristina Nascimento", "Female");
	}

	protected static Person isis() {
		return new Person("IN", "Isis Nascimento", "Female");
	}

	protected static Person paulo() {
		return new Person("PM", "Paulo Meirelles", "Male");
	}

	private PersonFixtures() {
		return;
	}
}