package org.kalibro.desktop.old.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.desktop.swingextension.icon.KalibroIcon;
import org.kalibro.desktop.old.StatusBar;
import org.kalibro.desktop.old.listeners.ProjectFrameListener;

public class ProjectFrame extends JInternalFrame {

	private ProjectPanel projectPanel;
	private ResultPanel resultPanel;

	private JTabbedPane tabbedPane;
	private StatusBar statusBar;

	private ProjectFrameListener listener;

	public ProjectFrame(ProjectFrameListener listener) {
		super("Project", true, true, true, true);
		this.listener = listener;
		initialize();
		setName("projectFrame");
		setVisible(true);
	}

	public Project project() {
		return projectPanel.project();
	}

	public void project(Project project) {
		projectPanel.project(project);
	}

	public void projectState(Project project, ProjectState state) {
		if (state.isTemporary())
			statusBar.temporaryText(state.getMessage("" + project));
		else
			statusBar.text(state.getMessage("" + project));
		statusBar.textColor( (state == ProjectState.ERROR) ? Color.RED : Color.BLACK);
	}

	public void result(ProjectResult result) {
		resultPanel.result(result);
		showResultsTab();
	}

	public void result(ProjectResult result, Configuration configuration) {
		resultPanel.resultTree(result, configuration);
		showResultsTab();
	}

	private void showResultsTab() {
		tabbedPane.setEnabledAt(1, true);
		tabbedPane.setSelectedIndex(1);
	}

	private void initialize() {
		setFrameIcon(new KalibroIcon().scaleForInternalFrame());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addInternalFrameListener(new InternalFrameAdapter() {

			@Override
			public void internalFrameClosing(InternalFrameEvent event) {
				listener.frameClosing();
			}
		});
		createComponents();
		Dimension size = resultPanel.getMinimumSize();
		setMinimumSize(size);
		setSize(size);
		buildPanel();
	}

	private void createComponents() {
		projectPanel = new ProjectPanel(listener);
		resultPanel = new ResultPanel(listener);

		tabbedPane = new JTabbedPane();
		statusBar = new StatusBar();
	}

	private void buildPanel() {
		tabbedPane.add("Project", projectPanel);
		tabbedPane.add("Results", resultPanel);
		tabbedPane.setEnabledAt(1, false);

		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}
}