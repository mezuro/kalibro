package org.kalibro.desktop.swingextension.field;

import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.io.IOUtils;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class TextFieldManualTest extends JPanel {

	private static final int LINES = 10;
	private static final int COLUMNS = 15;

	public static void main(String[] args) throws IOException {
		new ComponentWrapperDialog("TextField", new TextFieldManualTest()).setVisible(true);
	}

	private String text;

	private TextFieldManualTest() throws IOException {
		super();
		text = IOUtils.toString(getClass().getResourceAsStream("table.html"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(label(), 4);
		builder.newLine();
		builder.add(new Label("Plain text, no title:"));
		builder.add(textField(), 1, 2);
		builder.add(new Label("Html text, no title:"));
		builder.add(textField().showingHtml(), 1, 2);
		builder.newLine();
		builder.add(new Label("Plain text, titled:"));
		builder.add(textField().titled("Title"), 1, 2);
		builder.add(new Label("Html text, titled:"));
		builder.add(textField().titled("Title").showingHtml(), 1, 2);
	}

	private Label label() {
		Label label = new Label("All text fields have " + LINES + " lines and " + COLUMNS + " columns");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	private TextField textField() {
		TextField textField = new TextField("", LINES, COLUMNS);
		textField.set(text);
		return textField;
	}
}