package org.kalibro.desktop.swingextension.renderer;

import java.util.Collection;

import javax.swing.JLabel;

public class CollectionRenderer extends StandardRenderer {

	@Override
	public boolean canRender(Object value) {
		return value instanceof Collection<?>;
	}

	@Override
	public JLabel render(Object value) {
		String string = stringFor((Collection<?>) value);
		return new StringRenderer().render(string);
	}

	private String stringFor(Collection<?> collection) {
		if (collection.isEmpty())
			return "";
		String string = "";
		for (Object element : collection)
			string += ", " + element;
		return string.substring(2);
	}
}