package org.kalibro.core.model.enums;


/**
 * Represents a software language.
 * 
 * @author Carlos Morais
 */
public enum Language {

	C("C"),
	CPP("C++"),
	JAVA("Java");

	private String name;

	private Language(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}