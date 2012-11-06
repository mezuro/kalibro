package org.kalibro.desktop.swingextension.list;

import static org.kalibro.tests.UnitTest.loadFixture;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.JPanel;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class ListManualTest extends JPanel implements ActionListener, ListListener<Reading> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("List<Range>", new ListManualTest()).setVisible(true);
	}

	private List<Reading> list;

	private ListManualTest() {
		super(new BorderLayout());
		Collection<Reading> data = loadFixture("scholar", ReadingGroup.class).getReadings();
		list = new List<Reading>("", data, 8);
		list.addListListener(this);
		Button button = new Button("", "Clear selection", this);

		add(list, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
	}

	@Override
	public void doubleClicked(Reading reading) {
		System.out.println("Double clicked " + reading);
	}

	@Override
	public void selected(Reading reading) {
		System.out.println("Selected " + reading);
	}

	@Override
	public void selectionCleared() {
		System.out.println("Selection cleared");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JList<Reading> jList = (JList<Reading>) list.getViewport().getView();
		jList.clearSelection();
	}
}