package org.kalibro.desktop;

import static org.kalibro.desktop.KalibroIcons.*;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

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
		setJMenuBar(menuBar);
	}

	private void createPane() {
		tabbedPane = new JTabbedPane();
		setContentPane(tabbedPane);
	}

	private void setSize() {
		setMinimumSize(new Dimension(900, 700));
		setExtendedState(MAXIMIZED_BOTH);
	}
}