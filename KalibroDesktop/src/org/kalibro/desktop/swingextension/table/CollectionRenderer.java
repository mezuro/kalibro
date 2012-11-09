package org.kalibro.desktop.swingextension.table;

import java.util.Collection;

import javax.swing.JLabel;

class CollectionRenderer extends StandardRenderer {

	@Override
	boolean canRender(Object value) {
		return value instanceof Collection<?>;
	}

	@Override
	JLabel render(Object value) {
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