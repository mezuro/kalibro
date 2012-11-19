package org.kalibro.desktop.configuration;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.RendererUtil;
import org.kalibro.desktop.swingextension.field.FieldSize;
import org.kalibro.desktop.swingextension.table.DefaultRenderer;

class ReadingListRenderer implements ListCellRenderer<Reading> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Reading> list, Reading reading, int index,
		boolean isSelected, boolean hasFocus) {
		DefaultRenderer defaultRenderer = new DefaultRenderer();
		Component component = defaultRenderer.render(reading);
		if (reading == null)
			component = defaultRenderer.render("<none>");
		else
			component.setBackground(reading.getColor());
		component.setSize(new FieldSize(component));
		RendererUtil.setSelectionBackground(component, isSelected);
		return component;
	}
}