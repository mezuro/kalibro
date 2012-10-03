package org.kalibro.desktop.configuration;

import java.awt.Component;

import org.kalibro.Range;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;

public class RangeFieldRenderer extends DefaultRenderer {

	@Override
	public Component render(Object value, Object context) {
		Range range = (Range) context;
		Component component = super.render(value, context);
		component.setBackground(range.getReading().getColor());
		return component;
	}
}