package org.kalibro.core;

import java.util.Arrays;

/**
 * Converts strings across different formats: variable and class name (camel case); constant (upper case separated by
 * underscore); text (separated by spaces with first word capitalized).
 * 
 * @author Carlos Morais
 */
public final class Identifier {

	public static Identifier fromClassName(String className) {
		return fromVariable(className);
	}

	public static Identifier fromVariable(String variable) {
		String transformed = variable;
		for (String separation : Arrays.asList("([a-z])([A-Z])", "([a-zA-Z])([0-9])", "([0-9])([a-zA-Z])"))
			transformed = transformed.replaceAll(separation, "$1_$2");
		return fromConstant(transformed.toUpperCase());
	}

	public static Identifier fromText(String text) {
		return fromConstant(text
			.replace('_', ' ').replaceAll("[^A-Za-z0-9\\s]", "").trim()
			.replaceAll("\\s+", " ").replace(' ', '_').toUpperCase());
	}

	public static Identifier fromConstant(String constant) {
		return new Identifier(constant);
	}

	private String constant;

	private Identifier(String constant) {
		this.constant = constant;
	}

	public String asClassName() {
		String[] words = constant.toLowerCase().split("_");
		String className = "";
		for (String word : words)
			className += Character.toUpperCase(word.charAt(0)) + word.substring(1);
		return className;
	}

	public String asVariable() {
		String className = asClassName();
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}

	public String asText() {
		return constant.charAt(0) + constant.substring(1).toLowerCase().replace('_', ' ');
	}

	public String asConstant() {
		return constant;
	}
}