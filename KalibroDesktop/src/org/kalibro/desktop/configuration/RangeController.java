package org.kalibro.desktop.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public class RangeController implements ActionListener {

	private MetricConfiguration configuration;
	private Range range;

	private RangeDialog dialog;

	public RangeController(MetricConfiguration configuration) {
		this.configuration = configuration;
		dialog = new RangeDialog();
		dialog.addOkListener(this);
	}

	public void addRange() {
		range = null;
		dialog.setRange(new Range());
		dialog.setVisible(true);
	}

	public void editRange(Range theRange) {
		range = theRange;
		dialog.setRange(theRange);
		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			removeOldRange();
			configuration.addRange(dialog.getRange());
			dialog.dispose();
		} catch (Exception exception) {
			putOldRangeBack();
			new ErrorDialog(dialog).show(exception);
		}
	}

	private void removeOldRange() {
		if (range != null)
			configuration.removeRange(range);
	}

	private void putOldRangeBack() {
		if (range != null)
			configuration.addRange(range);
	}
}