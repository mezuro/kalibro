package org.kalibro.desktop.swingextension.list;

import static org.kalibro.tests.UnitTest.loadFixture;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.JPanel;

import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Button;

public final class ListManualTest extends JPanel implements ActionListener, ListListener<MetricConfiguration> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("List<MetricConfiguration>", new ListManualTest()).setVisible(true);
	}

	private List<MetricConfiguration> list;

	private ListManualTest() {
		super(new BorderLayout());
		Collection<MetricConfiguration> data = loadFixture("sc", Configuration.class).getMetricConfigurations();
		list = new List<MetricConfiguration>("", data, 8);
		list.addListListener(this);
		Button button = new Button("", "Clear selection", this);

		add(list, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
	}

	@Override
	public void doubleClicked(MetricConfiguration range) {
		System.out.println("Double clicked " + range);
	}

	@Override
	public void selected(MetricConfiguration range) {
		System.out.println("Selected " + range);
	}

	@Override
	public void selectionCleared() {
		System.out.println("Selection cleared");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JList jList = (JList) list.getViewport().getView();
		jList.clearSelection();
	}
}