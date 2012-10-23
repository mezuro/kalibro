package org.kalibro.desktop.swingextension.field;

import java.awt.Component;

import org.kalibro.KalibroException;
import org.kalibro.desktop.swingextension.panel.EditPanel;

public class PalindromeField extends EditPanel<String> {

	private StringField field;

	public PalindromeField() {
		super("palindrome");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		field = new StringField("palindrome", 15);
	}

	@Override
	protected void buildPanel() {
		add(field);
	}

	@Override
	public String get() {
		String text = field.get();
		String reverse = new StringBuffer(text).reverse().toString();
		if (text.equals(reverse))
			return text;
		throw new KalibroException("Not a palindrome");
	}

	@Override
	public void set(String value) {
		field.set(value);
	}
}