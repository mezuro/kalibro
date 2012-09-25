package org.kalibro;

import org.kalibro.core.Identifier;

/**
 * Represents a software language.
 * 
 * @author Carlos Morais
 */
public enum Language {

	C, CPP("C++"), JAVA, PYTHON;

	private String name;

	private Language() {
		name = Identifier.fromConstant(name()).asText();
	}

	private Language(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}