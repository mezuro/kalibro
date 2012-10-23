package org.kalibro.desktop.swingextension.renderer;

import java.awt.Component;

public abstract class StandardRenderer extends TableRenderer {

	protected abstract boolean canRender(Object value);

	@Override
	protected Component render(Object value, Object context) {
		return render(value);
	}

	protected abstract Component render(Object value);
}