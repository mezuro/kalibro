package org.kalibro.desktop.swingextension;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class TreeRenderer implements TreeCellRenderer {

	@Override
	public JLabel getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean isExpanded,
		boolean isLeaf, int row, boolean hasFocus) {
		String text = getText(value);
		Icon icon = getTreeIcon(isLeaf, isExpanded);
		JLabel label = createLabel(text, icon, tree.getBackground());
		RendererUtil.setSelectionBackground(label, isSelected);
		return label;
	}

	private String getText(Object value) {
		return value == null ? "" : value.toString();
	}

	private JLabel createLabel(String text, Icon icon, Color background) {
		JLabel label = new JLabel(text, icon, SwingConstants.LEFT);
		label.setOpaque(true);
		label.setBackground(background);
		return label;
	}

	private Icon getTreeIcon(boolean isLeaf, boolean isExpanded) {
		DefaultTreeCellRenderer nativeRenderer = new DefaultTreeCellRenderer();
		if (isLeaf)
			return nativeRenderer.getLeafIcon();
		else if (isExpanded)
			return nativeRenderer.getOpenIcon();
		else
			return nativeRenderer.getClosedIcon();
	}
}