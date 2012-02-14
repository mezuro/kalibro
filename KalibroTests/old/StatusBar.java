package org.kalibro.desktop.old;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.kalibro.desktop.swingextension.Label;

public class StatusBar extends JPanel implements Runnable {

	private Label label;

	private String text;
	private Thread thread;

	public StatusBar() {
		label = new Label("");
		label.setName("status");
		thread = new Thread(this);
		buildPanel();
	}

	private void buildPanel() {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(0, label.getSize().height));
		add(label);
	}

	public void textColor(Color color) {
		label.setForeground(color);
	}

	public void text(String aText) {
		thread.interrupt();
		this.text = aText;
		label.setText(aText);
	}

	public void temporaryText(String aText) {
		thread.interrupt();
		this.text = aText;
		label.setText(aText + "   ");
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			String[] suffix = {"   ", ".  ", ".. ", "..."};
			for (int i = 0; true; i++, i %= 4) {
				label.setText(text + suffix[i]);
				Thread.sleep(500);
			}
		} catch (InterruptedException exception) {
			// end of thread
		}
	}
}