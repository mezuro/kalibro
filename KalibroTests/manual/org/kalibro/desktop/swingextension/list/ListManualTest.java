package org.kalibro.desktop.swingextension.list;

import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.util.SortedSet;

import org.kalibro.core.model.Range;
import org.kalibro.desktop.ComponentWrapperDialog;

public class ListManualTest implements ListListener<Range> {

	public static void main(String[] args) {
		new ComponentWrapperDialog("List<Range>", createList()).setVisible(true);
		System.exit(0);
	}

	public static List<Range> createList() {
		SortedSet<Range> ranges = configuration("amloc").getRanges();
		List<Range> list = new List<Range>("", ranges, 5);
		list.addListListener(new ListManualTest());
		return list;
	}

	@Override
	public void doubleClicked(Range range) {
		System.out.println("Double clicked " + range);
	}

	@Override
	public void selected(Range range) {
		System.out.println("Selected " + range);
	}

	@Override
	public void selectionCleared() {
		System.out.println("Selection cleared");
	}
}