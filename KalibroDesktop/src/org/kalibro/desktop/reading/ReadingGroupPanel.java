package org.kalibro.desktop.reading;

import java.awt.Component;
import java.util.TreeSet;

import javax.swing.border.TitledBorder;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.panel.ConfirmPanel;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;
import org.kalibro.desktop.swingextension.panel.TablePanel;
import org.kalibro.desktop.swingextension.table.Table;

public class ReadingGroupPanel extends EditPanel<ReadingGroup> {

	private StringField nameField;
	private TextField descriptionField;
	private TablePanel<Reading> readingTablePanel;
	private ConfirmPanel<Reading> readingPanel;

	private ReadingController controller;

	public ReadingGroupPanel() {
		super("readingGroup");
		controller = new ReadingController(this);
		readingTablePanel.addTablePanelListener(controller);
		readingPanel.addOkListener(controller);
		readingPanel.addCancelListener(controller);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		nameField = new StringField("name", 25);
		descriptionField = new TextField("description", 6, 20);
		createReadingTablePanel();
		readingPanel = new ConfirmPanel<Reading>(new ReadingPanel());
		readingPanel.setBorder(new TitledBorder(""));
	}

	private void createReadingTablePanel() {
		Table<Reading> table = new Table<Reading>("readings", 9, Reading.class);
		table.addColumn("grade").withWidth(5).renderedBy(new ReadingFieldRenderer());
		table.addColumn("label").withWidth(20).renderedBy(new ReadingFieldRenderer());
		table.pack();
		readingTablePanel = new TablePanel<Reading>(table);
		readingTablePanel.setBorder(new TitledBorder("Readings"));
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("Name:"), nameField);
		builder.addSimpleLine(new Label("Description:"), descriptionField);
		builder.add(readingTablePanel, 2);
		builder.newLine();
		builder.add(readingPanel, 2);
		hideReadingPanel();
	}

	@Override
	public ReadingGroup get() {
		ReadingGroup model = controller.getReadingGroup();
		model.setName(nameField.get());
		model.setDescription(descriptionField.get());
		model.setReadings(new TreeSet<Reading>(readingTablePanel.get()));
		return model;
	}

	@Override
	public void set(ReadingGroup readingGroup) {
		nameField.set(readingGroup.getName());
		descriptionField.set(readingGroup.getDescription());
		readingTablePanel.set(readingGroup.getReadings());
		controller.setReadingGroup(readingGroup);
	}

	void showReadingPanel(String title, Reading reading) {
		((TitledBorder) readingPanel.getBorder()).setTitle(title);
		readingPanel.set(reading);
		readingPanel.setVisible(true);
	}

	void hideReadingPanel() {
		readingPanel.setVisible(false);
	}

	Reading getReading() {
		return readingPanel.get();
	}
}