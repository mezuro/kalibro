package org.kalibro.desktop.swingextension.list;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class List<T> extends JScrollPane implements ListComponent<T> {

	private JList<T> list;

	public List(String name, Collection<T> data, int lines) {
		super();
		setName(name);
		createList(data, name);
		changeHeight(lines);
	}

	private void createList(Collection<T> data, String name) {
		list = new JList<T>(new Vector<T>(data));
		list.setName(name);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setViewportView(list);
	}

	private void changeHeight(int lines) {
		Dimension size = getMinimumSize();
		size.width += list.getMinimumSize().width;
		size.height = lines * (1 + new JTable().getRowHeight());
		setMinimumSize(size);
		setPreferredSize(size);
	}

	@Override
	public boolean hasSelection() {
		return list.getSelectedIndex() != -1;
	}

	@Override
	public T getSelected() {
		return list.getSelectedValue();
	}

	public void addListListener(ListListener<T> listener) {
		ListComponentAdapter<T> adapter = new ListComponentAdapter<T>(listener, this);
		list.addMouseListener(adapter);
		list.getSelectionModel().addListSelectionListener(adapter);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		list.setEnabled(enabled);
	}
}