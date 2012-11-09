package org.kalibro.desktop.swingextension.table;

import java.awt.Component;

abstract class StandardRenderer extends ColumnRenderer {

	abstract boolean canRender(Object value);

	@Override
	protected Component render(Object value, Object row) {
		return render(value);
	}

	abstract Component render(Object value);
}