package org.kalibro.desktop.reading;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.panel.TablePanelListener;

class ReadingController implements ActionListener, TablePanelListener<Reading> {

	private ReadingGroupPanel groupPanel;
	private ReadingGroup group;
	private boolean add;

	ReadingController(ReadingGroupPanel groupPanel) {
		this.groupPanel = groupPanel;
	}

	ReadingGroup getReadingGroup() {
		if (group == null)
			group = new ReadingGroup();
		return group;
	}

	void setReadingGroup(ReadingGroup readingGroup) {
		this.group = readingGroup;
	}

	@Override
	public void add() {
		add = true;
		groupPanel.showReadingPanel("Add reading", new Reading());
	}

	@Override
	public void edit(Reading element) {
		add = false;
		groupPanel.showReadingPanel("Edit reading", element);
	}

	@Override
	public void remove(Reading element) {
		groupPanel.hideReadingPanel();
		group.removeReading(element);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String name = ((Component) event.getSource()).getName();
		if (name.equals("cancel"))
			groupPanel.hideReadingPanel();
		else if (editConfirmed()) {
			groupPanel.set(group);
			groupPanel.hideReadingPanel();
		}
	}

	private boolean editConfirmed() {
		try {
			updateModel();
			return true;
		} catch (Exception exception) {
			new ErrorDialog(groupPanel).show(exception);
			return false;
		}
	}

	private void updateModel() {
		Reading reading = groupPanel.getReading();
		if (add)
			group.addReading(reading);
	}
}