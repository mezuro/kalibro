package org.kalibro.desktop;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import org.kalibro.desktop.swingextension.Icon;

public class KalibroFrame extends JFrame {

	public KalibroFrame() {
		super("Kalibro");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIconImage(new Icon(Icon.KALIBRO).getImage());
		setName("kalibroFrame");
		createMenuBar();
		setSize();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new KalibroMenu());
		setJMenuBar(menuBar);
	}

	private void setSize() {
		setMinimumSize(new Dimension(900, 700));
		setExtendedState(MAXIMIZED_BOTH);
	}
}