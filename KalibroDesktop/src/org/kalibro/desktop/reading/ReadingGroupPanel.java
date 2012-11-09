package org.kalibro.desktop.reading;

import java.awt.Component;
import java.util.TreeSet;

import javax.swing.border.TitledBorder;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.swingextension.table.Table;
import org.kalibro.desktop.swingextension.table.TablePanel;
import org.kalibro.desktop.swingextension.table.TablePanelController;

public class ReadingGroupPanel extends EditPanel<ReadingGroup> implements TablePanelController<Reading> {

	private StringField nameField;
	private TextField descriptionField;
	private TablePanel<Reading> readingsPanel;

	private ReadingGroup model;

	public ReadingGroupPanel(ReadingGroup group) {
		super("metricConfiguration");
		set(group);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		nameField = new StringField("name", 25);
		descriptionField = new TextField("description", 6, 20);
		createReadingsPanel();
	}

	private void createReadingsPanel() {
		Table<Reading> table = new Table<Reading>("readings", 9, Reading.class);
		table.addColumn("grade").withWidth(5).renderedBy(new ReadingFieldRenderer());
		table.addColumn("label").withWidth(20).renderedBy(new ReadingFieldRenderer());
		table.pack();
		readingsPanel = new TablePanel<Reading>(this, table);
		readingsPanel.setBorder(new TitledBorder("Readings"));
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("Name:"), nameField);
		builder.addSimpleLine(new Label("Description:"), descriptionField);
		builder.add(readingsPanel, 2);
	}

	@Override
	public ReadingGroup get() {
		model.setName(nameField.get());
		model.setDescription(descriptionField.get());
		model.setReadings(new TreeSet<Reading>(readingsPanel.get()));
		return model;
	}

	@Override
	public void set(ReadingGroup group) {
		nameField.set(group.getName());
		descriptionField.set(group.getDescription());
		readingsPanel.set(group.getReadings());
		model = group;
	}

	@Override
	public Reading add() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reading edit(Reading element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Reading element) {
		// TODO Auto-generated method stub

	}
}