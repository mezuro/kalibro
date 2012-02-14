package org.kalibro.desktop.swingextension.field;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class TextField extends JScrollPane implements Field<String> {

	private JTextPane textPane;

	public TextField(String name, int lines, int columns) {
		this(name, lines, columns, null);
	}

	public TextField(String name, int lines, int columns, String title) {
		this(name, lines, columns, title, false);
	}

	public TextField(String name, int lines, int columns, String title, boolean showHtml) {
		super();
		setName(name);
		createPane();
		setTitle(title);
		setShowHtml(showHtml);
		changeSize(lines, columns);
	}

	private void createPane() {
		textPane = new JTextPane();
		textPane.setName(getName());
		textPane.setBorder(new StringField("", 0).getBorder());
		setViewportView(textPane);
	}

	private void setTitle(String title) {
		if (title == null || title.trim().isEmpty())
			setBorder(null);
		else
			setBorder(new TitledBorder(title));
	}

	public void setShowHtml(boolean showHtml) {
		textPane.setEditable(! showHtml);
		textPane.setContentType(showHtml ? "text/html" : "text/plain");
	}

	public void changeSize(int lines, int columns) {
		int width = new StringField("", columns).getPreferredSize().width;
		double height = 2 * getTitleFontSize() + (1 + 1.25 * lines) * getTextFontSize();
		Dimension size = new Dimension(width, (int) height);
		setMinimumSize(size);
		setPreferredSize(size);
	}

	private int getTitleFontSize() {
		Border border = getBorder();
		if (! (border instanceof TitledBorder))
			return 0;
		return ((TitledBorder) border).getTitleFont().getSize();
	}

	private int getTextFontSize() {
		return textPane.getFont().getSize();
	}

	@Override
	public String getValue() {
		return textPane.getText().trim();
	}

	@Override
	public void setValue(String text) {
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