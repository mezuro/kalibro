package org.kalibro.desktop.configuration;

import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ColorField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class RangePanel extends EditPanel<Range> {

	private DoubleField beginningField;
	private DoubleField endField;
	private StringField labelField;
	private DoubleField gradeField;
	private ColorField colorField;
	private TextField commentsField;

	public RangePanel() {
		super("range");
	}

	@Override
	protected void createComponents() {
		beginningField = new DoubleField("beginning", Double.NEGATIVE_INFINITY);
		endField = new DoubleField("end", Double.POSITIVE_INFINITY);
		labelField = new StringField("label", 20);
		gradeField = new DoubleField("grade");
		colorField = new ColorField("color");
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
	public void show(Range range) {
		beginningField.setValue(range.getBeginning());
		endField.setValue(range.getEnd());
		labelField.setText(range.getLabel());
		gradeField.setValue(range.getGrade());
		colorField.setValue(range.getColor());
		commentsField.setValue(range.getComments());
	}

	@Override
	public Range get() {
		Range range = new Range(beginningField.getValue(), endField.getValue());
		range.setLabel(labelField.getText());
		range.setGrade(gradeField.getValue());
		range.setColor(colorField.getValue());
		range.setComments(commentsField.getValue());
		return range;
	}
}