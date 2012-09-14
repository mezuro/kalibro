package org.kalibro.desktop.reading;

import java.awt.Component;

import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ColorField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

class ReadingPanel extends EditPanel<Reading> {

	private StringField labelField;
	private DoubleField gradeField;
	private ColorField colorField;

	private Reading model;

	public ReadingPanel(Reading reading) {
		super("model");
		this.model = reading;
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		labelField = new StringField("label", 20);
		gradeField = new DoubleField("grade");
		colorField = new ColorField("color");
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(new Label("Label:"));
		builder.add(labelField, 3);
		builder.newLine();
		builder.add(new Label("Grade:"));
		builder.add(gradeField);
		builder.add(colorField, 2);
	}

	@Override
	public Reading get() {
		model.setLabel(labelField.getText());
		model.setGrade(gradeField.get());
		model.setColor(colorField.get());
		return model;
	}

	@Override
	public void set(Reading reading) {
		labelField.setText(reading.getLabel());
		gradeField.set(reading.getGrade());
		colorField.set(reading.getColor());
		this.model = reading;
	}
}