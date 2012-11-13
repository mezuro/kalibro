package org.kalibro.desktop;

import static org.kalibro.desktop.KalibroIcons.*;

import java.awt.Dimension;

import javax.swing.*;

import org.kalibro.desktop.reading.ReadingGroupController;
import org.kalibro.desktop.swingextension.panel.EditPanel;

public final class KalibroFrame extends JFrame {

	private static KalibroFrame instance = new KalibroFrame();

	public static KalibroFrame getInstance() {
		return instance;
	}

	private JTabbedPane tabbedPane;

	private KalibroFrame() {
		super("Kalibro");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIconImage(image(KALIBRO));
		setName("kalibroFrame");
		createMenuBar();
		createPane();
		setSize();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new KalibroMenu());
		menuBar.add(new CrudMenu(new ReadingGroupController(this)));
		setJMenuBar(menuBar);
	}

	private void createPane() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setName("tabbedPane");
		setContentPane(tabbedPane);
	}

	private void setSize() {
		setMinimumSize(new Dimension(900, 700));
		setExtendedState(MAXIMIZED_BOTH);
	}

	void addTab(String title, EditPanel<?> panel) {
		tabbedPane.add(title, new JScrollPane(panel));
	}

	String getSelectedTitle() {
		int index = tabbedPane.getSelectedIndex();
		return (index == -1) ? "" : tabbedPane.getTitleAt(index);
	}

	<T> EditPanel<T> getSelectedTab() {
		JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
		return (EditPanel<T>) scrollPane.getViewport().getView();
	}

	void setSelectedTitle(String title) {
		tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), title);
	}

	void removeSelectedTab() {
		tabbedPane.remove(tabbedPane.getSelectedIndex());
	}
}