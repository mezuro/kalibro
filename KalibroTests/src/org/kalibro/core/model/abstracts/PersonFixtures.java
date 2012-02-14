package org.kalibro.core.model.abstracts;

public class PersonFixtures {

	public static Person carlos() {
		Person carlos = new Person("CM", "Carlos Morais", "Male");
		carlos.addRelative("mother", cristina());
		carlos.addRelative("sister", isis());
		return carlos;
	}

	public static Person cristina() {
		return new Person("CN", "Cristina Nascimento", "Female");
	}

	public static Person isis() {
		return new Person("IN", "Isis Nascimento", "Female");
	}

	public static Person paulo() {
		return new Person("PM", "Paulo Meirelles", "Male");
	}
}