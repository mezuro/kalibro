package org.kalibro.desktop.swingextension.field;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class TextFieldManualTest {

	private static final int LINES = 10;
	private static final int COLUMNS = 15;
	private static final String TEXT =
		"<tr>\n  <td>This</td>\n  <td>is</td>\n</tr>\n<tr>\n  <td>my</td>\n  <td>table.</td>\n</tr>";

	public static void main(String[] args) {
		new ComponentWrapperDialog("TextField", buildPanel()).setVisible(true);
	}

	private static JPanel buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.add(label(), 4);
		builder.newLine();
		builder.add(new Label("Plain text, no title:"));
		builder.add(getNoTitlePlain(), 1, 2);
		builder.add(new Label("Html text, no title:"));
		builder.add(getNoTitleHtml(), 1, 2);
		builder.newLine();
		builder.add(new Label("Plain text, titled:"));
		builder.add(getTitledPlain(), 1, 2);
		builder.add(new Label("Html text, titled:"));
		builder.add(getTitledHtml(), 1, 2);
		return builder.getPanel();
	}

	private static Label label() {
		Label label = new Label("All text fields have " + LINES + " lines and " + COLUMNS + " columns");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	private static TextField getNoTitlePlain() {
		return newTextField("", LINES, COLUMNS, "", false);
	}

	private static TextField getNoTitleHtml() {
		return newTextField("", LINES, COLUMNS, null, true);
	}

	private static TextField getTitledPlain() {
		return newTextField("", LINES, COLUMNS, "Title", false);
	}

	private static TextField getTitledHtml() {
		return newTextField("", LINES, COLUMNS, "Title", true);
	}

	private static TextField newTextField(String name, int lines, int columns, String title, boolean showHtml) {
		TextField textField = new TextField(name, lines, columns, title, showHtml);
		textField.setValue(TEXT);
		return textField;
	}
}