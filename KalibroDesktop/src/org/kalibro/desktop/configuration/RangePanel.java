package org.kalibro.desktop.configuration;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboPopup;

import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

class RangePanel extends EditPanel<Range> implements ActionListener {

	private DoubleField beginningField, endField;
	private ChoiceField<Reading> readingField;
	private TextField commentsField;

	private Range model;

	RangePanel() {
		super("range");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		beginningField = new DoubleField("beginning", Double.NEGATIVE_INFINITY);
		endField = new DoubleField("end", Double.POSITIVE_INFINITY);
		readingField = new ChoiceField<Reading>("reading", (Reading) null);
		readingField.setRenderer(new ReadingListRenderer());
		readingField.addActionListener(this);
		commentsField = new TextField("comments", 4, 20);
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("From"), beginningField, new Label("to"), endField);
		builder.add(new Label("Reading:"));
		builder.add(readingField, 3);
		builder.newLine();
		builder.add(new Label("Comments:"));
		builder.add(commentsField, 3, 2);
	}

	void setPossibleReadings(Collection<Reading> readings) {
		Vector<Reading> items = new Vector<Reading>(readings);
		items.add(0, null);
		readingField.setModel(new DefaultComboBoxModel<Reading>(items));
	}

	@Override
	public Range get() {
		if (model == null)
			model = new Range();
		model.set(beginningField.get(), endField.get());
		model.setReading(readingField.get());
		model.setComments(commentsField.get());
		return model;
	}

	@Override
	public void set(Range range) {
		beginningField.set(range.getBeginning());
		endField.set(range.getEnd());
		readingField.set(range.getReading());
		commentsField.set(range.getComments());
		model = range;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		BasicComboPopup comboPopup = (BasicComboPopup) readingField.getAccessibleContext().getAccessibleChild(0);
		comboPopup.getList().setSelectionBackground(selectionBackGroundFor(readingField.get()));
	}

	private Color selectionBackGroundFor(Reading reading) {
		return reading == null ? new JList<Object>().getSelectionBackground() : reading.getColor();
	}
}