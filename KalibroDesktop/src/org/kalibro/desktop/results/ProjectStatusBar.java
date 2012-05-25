package org.kalibro.desktop.results;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.kalibro.Kalibro;
import org.kalibro.core.ProjectStateListener;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.icon.Icon;

public class ProjectStatusBar extends JPanel implements ProjectStateListener {

	public ProjectStatusBar(Project project) {
		setLayout(new GridLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(0, new Label("").getSize().height));
		projectStateChanged(project.getName(), project.getState());
		Kalibro.addProjectStateListener(project, this);
	}

	@Override
	public void projectStateChanged(String projectName, ProjectState newProjectState) {
		String icon = "";
		if (newProjectState == ProjectState.ERROR)
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