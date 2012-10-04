package org.kalibro.desktop.results;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.kalibro.Project;
import org.kalibro.RepositoryState;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.icon.Icon;

public class ProjectStatusBar extends JPanel {

	private String projectName;

	public ProjectStatusBar(Project project) {
		setLayout(new GridLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(0, new Label("").getSize().height));
		projectName = project.getName();
		setProjectState(project.getState());
	}

	public void setProjectState(RepositoryState newProjectState) {
		String icon = "";
		if (newProjectState == RepositoryState.ERROR)
			icon = "error.gif";
		else if (newProjectState.isTemporary())
			icon = "waiting.gif";
		setText(newProjectState.getMessage(projectName), icon);
	}

	private void setText(String text, String icon) {
		Label label = new Label(text);
		label.setIcon(new Icon(icon));
		removeAll();
		add(label);
		validate();
		repaint();
	}
}