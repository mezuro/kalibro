package org.kalibro.desktop.reading;

import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.list.*;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ReadingGroupPanel extends EditPanel<ReadingGroup> {

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
		ReflectionTableModel<Reading> tableModel = new ReflectionTableModel<Reading>(Reading.class);
		tableModel.addColumn(new ReflectionColumn("grade", 5, new ReadingFieldRenderer()));
		tableModel.addColumn(new ReflectionColumn("label", 20, new ReadingFieldRenderer()));
		Table<Reading> readingsTable = new Table<Reading>("readings", tableModel, 9);
		readingsPanel = new TablePanel<Reading>(readingsTable);
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
		model.setReadings(readingsPanel.get());
		return model;
	}

	@Override
	public void set(ReadingGroup group) {
		nameField.set(group.getName());
		descriptionField.set(group.getDescription());
		readingsPanel.set(group.getReadings());
		model = group;
	}

	public void addReadingsListener(TablePanelListener<Reading> listener) {
		readingsPanel.addTablePanelListener(listener);
	}
}