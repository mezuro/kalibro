package org.kalibro.desktop.swingextension.table;

import java.awt.Component;

abstract class StandardRenderer extends ColumnRenderer {

	abstract boolean canRender(Object value);

	@Override
	public Component render(Object value, Object row) {
		return render(value);
	}

	public abstract Component render(Object value);
}