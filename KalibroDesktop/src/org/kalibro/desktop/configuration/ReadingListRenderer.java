package org.kalibro.desktop.configuration;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.RendererUtil;
import org.kalibro.desktop.swingextension.table.DefaultRenderer;

class ReadingListRenderer implements ListCellRenderer<Reading> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Reading> list, Reading reading, int index,
		boolean isSelected, boolean hasFocus) {
		Component component = new DefaultRenderer().render(reading);
		component.setBackground(reading.getColor());
		RendererUtil.setSelectionBackground(component, isSelected);
		return component;
	}
}