package org.kalibro.desktop.configuration;

import java.awt.Component;

import org.kalibro.Range;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ColorField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class RangePanel extends EditPanel<Range> {

	private DoubleField beginningField, endField;
	private StringField labelField;
	private DoubleField gradeField;
	private ColorField colorField;
	private TextField commentsField;

	public RangePanel() {
		super("range");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		beginningField = new DoubleField("beginning", Double.NEGATIVE_INFINITY);
		endField = new DoubleField("end", Double.POSITIVE_INFINITY);
		labelField = new StringField("label", 20);
		gradeField = new DoubleField("grade");
		colorField = new ColorField();
		commentsField = new TextField("comments", 4, 20);
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("From"), beginningField, new Label("to"), endField);
		builder.add(new Label("Label:"));
		builder.add(labelField, 3);
		builder.newLine();
		builder.add(new Label("Grade:"));
		builder.add(gradeField);
		builder.add(colorField, 2);
		builder.newLine();
		builder.add(new Label("Comments:"));
		builder.add(commentsField, 3, 2);
	}

	@Override
	public Range get() {
		Range range = new Range(beginningField.get(), endField.get());
		range.getReading().setLabel(labelField.getText());
		range.getReading().setGrade(gradeField.get());
		range.getReading().setColor(colorField.get());
		range.setComments(commentsField.get());
		return range;
	}

	@Override
	public void set(Range range) {
		beginningField.set(range.getBeginning());
		endField.set(range.getEnd());
		labelField.setText(range.getReading().getLabel());
		gradeField.set(range.getReading().getGrade());
		colorField.set(range.getReading().getColor());
		commentsField.set(range.getComments());
	}
}