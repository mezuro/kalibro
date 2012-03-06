package org.kalibro.desktop.swingextension.field;

public class PalindromeField extends StringField {

	public PalindromeField() {
		super("palindrome", 15);
	}

	@Override
	public String get() {
		String text = getText();
		String reverse = new StringBuffer(text).reverse().toString();
		if (text.equals(reverse))
			return text;
		throw new RuntimeException("Not a palindrome");
	}
}