package org.kalibro.desktop.swingextension.list;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.panel.EditPanel;

public class TablePanel<T> extends EditPanel<Collection<T>> {

	private Table<T> table;
	private Button addButton;
	private Button editButton;
	private Button removeButton;

	public TablePanel(Table<T> table) {
		super(table.getName());
		this.table = table;
		table.addListListener(new EnableButtonsListener());
		add(table, BorderLayout.CENTER);
	}

	@Override
	protected void createComponents() {
		addButton = new Button("add", "Add");
		editButton = new Button("edit", "Edit");
		removeButton = new Button("remove", "Remove", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				table.remove(table.getSelectedObject());
			}
		});
		enableButtons(false);
	}

	@Override
	protected void buildPanel() {
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.add(addButton);
		southPanel.add(editButton);
		southPanel.add(removeButton);
		setLayout(new BorderLayout());
		add(southPanel, BorderLayout.SOUTH);
	}

	@Override
	public void show(Collection<T> data) {
		table.setData(data);
	}

	@Override
	public List<T> retrieve() {
		return table.getData();
	}

	private class EnableButtonsListener extends ListAdapter<T> {

		@Override
		public void selected(T rowObject) {
			enableButtons(true);
		}

		@Override
		public void selectionCleared() {
			enableButtons(false);
		}
	}

	private void enableButtons(boolean enable) {
		editButton.setEnabled(enable);
		removeButton.setEnabled(enable);
	}

	public void addTablePanelListener(TablePanelListener<T> listener) {
		ListenerAdapter adapter = new ListenerAdapter(listener);
		addButton.addActionListener(adapter);
		editButton.addActionListener(adapter);
		table.addListListener(adapter);
	}

	private class ListenerAdapter extends ListAdapter<T> implements ActionListener {

		private TablePanelListener<T> tablePanelListener;

		private ListenerAdapter(TablePanelListener<T> tablePanelListener) {
			this.tablePanelListener = tablePanelListener;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			Object source = event.getSource();
			if (source == addButton)
				tablePanelListener.add();
			else if (source == editButton)
				tablePanelListener.edit(table.getSelectedObject());
		}

		@Override
		public void doubleClicked(T row) {
			tablePanelListener.edit(row);
		}
	}
}