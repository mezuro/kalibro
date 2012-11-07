package org.kalibro.desktop.configuration;

import org.kalibro.KalibroException;
import org.kalibro.MetricConfiguration;
import org.kalibro.Range;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.EditDialogListener;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public class RangeController implements EditDialogListener<Range> {

	private MetricConfiguration configuration;
	private Range range;

	private EditDialog<Range> dialog;

	public RangeController(MetricConfiguration configuration) {
		this.configuration = configuration;
		dialog = new EditDialog<Range>(null, "Range", new RangePanel());
		dialog.setResizable(false);
		dialog.addListener(this);
	}

	public void addRange() {
		range = null;
		dialog.edit(new Range());
	}

	public void editRange(Range theRange) {
		range = theRange;
		dialog.edit(theRange);
	}

	@Override
	public boolean dialogConfirm(Range newRange) {
		try {
			confirmRange(newRange);
			return true;
		} catch (KalibroException exception) {
			new ErrorDialog(dialog).show(exception);
			return false;
		}
	}

	private void confirmRange(Range newRange) {
		configuration.removeRange(range);
		configuration.addRange(newRange);
	}
}