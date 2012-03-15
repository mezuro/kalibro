package org.kalibro.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import org.kalibro.desktop.swingextension.icon.Icon;

public class KalibroFrame extends JFrame {

	private JDesktopPane desktopPane;

	public KalibroFrame() {
		super("Kalibro");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIconImage(new Icon(Icon.KALIBRO).getImage());
		setName("kalibroFrame");
		createDesktopPane();
		createMenuBar();
		setSize();
	}

	private void createDesktopPane() {
		desktopPane = new JDesktopPane();
		setContentPane(desktopPane);
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new KalibroMenu());
		menuBar.add(new ConfigurationMenu(desktopPane));
		setJMenuBar(menuBar);
	}

	private void setSize() {
		setMinimumSize(new Dimension(800, 540));
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
	}
}