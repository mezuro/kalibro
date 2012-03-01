package org.kalibro.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.kalibro.desktop.swingextension.icon.Icon;

public class KalibroFrame extends JFrame {

	private KalibroFrameListener listener;

	public KalibroFrame(KalibroFrameListener listener) {
		super("Kalibro");
		this.listener = listener;
		setSize();
		createMenuBar();
		setContentPane(new JDesktopPane());
		setIconImage(new Icon(Icon.KALIBRO).getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void setSize() {
		setMinimumSize(new Dimension(800, 540));
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createSettingsMenuItem());
		setJMenuBar(menuBar);
	}

	private JMenuItem createSettingsMenuItem() {
		return newMenuItem("settings", "Settings", 'S', new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				listener.editSettings();
			}
		});
	}

	private JMenuItem newMenuItem(String name, String title, char mnemonic, ActionListener... listeners) {
		JMenuItem item = new JMenuItem(title);
		item.setMnemonic(mnemonic);
		item.setName(name);
		for (ActionListener actionListener : listeners)
			item.addActionListener(actionListener);
		return item;
	}
}