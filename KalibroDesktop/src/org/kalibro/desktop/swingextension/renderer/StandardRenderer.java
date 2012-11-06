package org.kalibro.desktop.swingextension.renderer;

import java.awt.Component;

abstract class StandardRenderer extends TableRenderer {

	abstract boolean canRender(Object value);

	@Override
	protected Component render(Object value, Object context) {
		return render(value);
	}

	abstract Component render(Object value);
}