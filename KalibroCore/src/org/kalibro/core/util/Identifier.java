package org.kalibro.core.util;

public final class Identifier {

	public static Identifier fromConstant(String constant) {
		return new Identifier(constant);
	}

	public static Identifier fromText(String text) {
		String constant = text.replaceAll("\\s+", "\\_").replaceAll("[^A-Za-z0-9_]", "").replaceAll("\\_+", "_");
		return new Identifier(constant.toUpperCase());
	}

	public static Identifier fromVariable(String variable) {
		String[] separations = {"([a-z])([A-Z])", "([a-z])([0-9])", "([0-9])([a-zA-Z])"};
		String constant = variable;
		for (String separation : separations)
			constant = constant.replaceAll(separation, "$1_$2");
		return new Identifier(constant.toUpperCase());
	}

	private String constant;

	private Identifier(String constant) {
		this.constant = constant;
	}

	public String asClassName() {
		String[] words = constant.toLowerCase().split("_");
		String variable = "";
		for (String word : words)
			variable += Character.toUpperCase(word.charAt(0)) + word.substring(1);
		return variable;
	}

	public String asConstant() {
		return constant;
	}

	public String asVariable() {
		String className = asClassName();
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}

	public String asText() {
		return constant.charAt(0) + constant.substring(1).toLowerCase().replace('_', ' ');
	}
}