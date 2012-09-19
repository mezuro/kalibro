package org.kalibro.desktop.reading;

import java.awt.Component;

import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;

class ReadingFieldRenderer extends DefaultRenderer {

	@Override
	public Component render(Object value, Object context) {
		Reading reading = (Reading) context;
		Component component = super.render(value, context);
		component.setBackground(reading.getColor());
		return component;
	}
}