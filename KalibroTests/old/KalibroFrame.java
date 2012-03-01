package org.kalibro.desktop.old;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.kalibro.desktop.swingextension.icon.KalibroIcon;
import org.kalibro.desktop.old.listeners.KalibroFrameListener;

public class KalibroFrame extends JFrame {

	private JDesktopPane desktop;

	private KalibroFrameListener listener;

	public KalibroFrame(KalibroFrameListener listener) {
		super("Kalibro");
		this.listener = listener;
		initialize();
	}

	public JDesktopPane desktopPane() {
		return desktop;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createMenuConfiguration());
		menuBar.add(createMenuProject());
		return menuBar;
	}

	private JMenu createMenuConfiguration() {
		JMenu menuConfiguration = menu("configuration", "Configuration", 'C');
		menuConfiguration.add(createNewConfigurationItem());
		menuConfiguration.add(createOpenConfigurationItem());
		menuConfiguration.add(createRemoveConfigurationItem());
		return menuConfiguration;
	}

	private JMenuItem createNewConfigurationItem() {
		JMenuItem menuItem = menuItem("newConfiguration", "New", 'N');
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.newConfiguration();
			}
		});
		return menuItem;
	}

	private JMenuItem createOpenConfigurationItem() {
		JMenuItem menuItem = menuItem("openConfiguration", "Open", 'O');
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.openConfiguration();
			}
		});
		return menuItem;
	}

	private JMenuItem createRemoveConfigurationItem() {
		JMenuItem menuItem = menuItem("removeConfiguration", "Remove", 'R');
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.removeConfiguration();
			}
		});
		return menuItem;
	}

	private JMenu createMenuProject() {
		JMenu menuProject = menu("project", "Project", 'P');
		menuProject.add(createNewProjectItem());
		menuProject.add(createOpenProjectItem());
		menuProject.add(createRemoveProjectItem());
		return menuProject;
	}

	private JMenuItem createNewProjectItem() {
		JMenuItem menuItem = menuItem("newProject", "New", 'N');
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.newProject();
			}
		});
		return menuItem;
	}

	private JMenuItem createOpenProjectItem() {
		JMenuItem menuItem = menuItem("openProject", "Open", 'O');
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.openProject();
			}
		});
		return menuItem;
	}

	private JMenuItem createRemoveProjectItem() {
		JMenuItem menuItem = menuItem("removeProject", "Remove", 'R');
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.removeProject();
			}
		});
		return menuItem;
	}
}