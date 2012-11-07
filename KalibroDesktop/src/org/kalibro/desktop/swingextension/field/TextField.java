package org.kalibro.desktop.swingextension.field;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import org.kalibro.desktop.swingextension.Label;

public class TextField extends JScrollPane implements Field<String> {

	private int lineCount, columnCount;

	private JTextPane textPane;

	public TextField(String name, int lines, int columns) {
		super();
		setName(name);
		createPane();
		changeSize(lines, columns);
	}

	private void createPane() {
		textPane = new JTextPane();
		textPane.setName(getName());
		textPane.setContentType("text/plain");
		textPane.setBorder(new StringField("", 0).getBorder());
		setViewportView(textPane);
	}

	public TextField titled(String title) {
		setBorder(new TitledBorder(title));
		changeSize(lineCount, columnCount);
		return this;
	}

	public TextField showingHtml() {
		String text = get();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		set(text);
		return this;
	}

	public void changeSize(int lines, int columns) {
		lineCount = lines;
		columnCount = columns;
		int width = new StringField("", columns).getPreferredSize().width;
		double height = titleHeight() + (1 + 1.25 * lines) * getTextFontSize();
		Dimension size = new Dimension(width, (int) height);
		setMinimumSize(size);
		setPreferredSize(size);
	}

	private int titleHeight() {
		if (getBorder() instanceof TitledBorder)
			return new Label("").getSize().height;
		return 0;
	}

	private int getTextFontSize() {
		return textPane.getFont().getSize();
	}

	@Override
	public String get() {
		return textPane.getText().trim();
	}

	@Override
	public void set(String text) {
		textPane.setText(text);
	}

	public void setEditable(boolean editable) {
		textPane.setEditable(editable);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textPane.setEnabled(enabled);
	}
}