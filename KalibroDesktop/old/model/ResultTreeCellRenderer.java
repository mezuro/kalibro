package org.kalibro.desktop.old.model;

import static org.kalibro.Granularity.*;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.kalibro.Granularity;
import org.kalibro.desktop.swingextension.icon.MethodIcon;
import org.kalibro.desktop.swingextension.renderer.Renderer;

public class ResultTreeCellRenderer extends Renderer {

	@Override
	public JLabel getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean isExpanded,
		boolean isLeaf, int row, boolean hasFocus) {
		JLabel label = super.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
		if (value != null) {
			Granularity granularity = ((SourceTreeNode) value).getModule().getGranularity();
			if (granularity == METHOD)
				label.setIcon(new MethodIcon());
			else if (granularity == CLASS)
				label.setIcon(new DefaultTreeCellRenderer().getLeafIcon());
		}
		return label;
	}

	@Override
	protected Component render(Object value, Object context) {
		return null;
	}
}