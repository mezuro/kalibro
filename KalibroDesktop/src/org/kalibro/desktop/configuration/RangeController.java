package org.kalibro.desktop.configuration;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.EditDialogListener;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public class RangeController implements EditDialogListener<Range> {

	private MetricConfiguration configuration;
	private Range range;

	private RangePanel panel;
	private EditDialog<Range> dialog;

	public RangeController(MetricConfiguration configuration) {
		this.configuration = configuration;
		panel = new RangePanel();
		dialog = new EditDialog<Range>("Range");
		dialog.setField(panel);
		dialog.addListener(this);
		dialog.setName("rangeDialog");
		dialog.setResizable(false);
	}

	public void addRange() {
		range = null;
		panel.set(new Range());
		dialog.setVisible(true);
	}

	public void editRange(Range theRange) {
		range = theRange;
		panel.set(theRange);
		dialog.setVisible(true);
	}

	@Override
	public boolean dialogConfirm(Range newRange) {
		try {
			confirmRange(newRange);
			return true;
		} catch (Exception exception) {
			new ErrorDialog(dialog).show(exception);
			return false;
		}
	}

	private void confirmRange(Range newRange) {
		if (range == null)
			configuration.addRange(newRange);
		else
			configuration.replaceRange(range.getBeginning(), newRange);
	}
}