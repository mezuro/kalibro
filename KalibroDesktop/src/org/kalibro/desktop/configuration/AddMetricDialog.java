package org.kalibro.desktop.configuration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import org.kalibro.CompoundMetric;
import org.kalibro.Metric;
import org.kalibro.NativeMetric;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.RadioButton;
import org.kalibro.desktop.swingextension.dialog.AbstractDialog;
import org.kalibro.desktop.swingextension.list.ListListener;

public class AddMetricDialog extends AbstractDialog implements ActionListener, ListListener<NativeMetric> {

	private RadioButton compoundRadio, nativeRadio;
	private ChooseNativeMetricPanel nativeMetricPanel;

	private Button cancelButton, okButton;

	public AddMetricDialog() {
		super(null, "Add Metric");
		setResizable(false);
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		createRadioButtons();
		createNativeMetricPanel();
		cancelButton = new Button("cancel", "Cancel", this);
		okButton = new Button("ok", "Ok");
	}

	private void createRadioButtons() {
		compoundRadio = new RadioButton("compound", "Compound metric", this);
		nativeRadio = new RadioButton("native", "NativeMetric", this);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(compoundRadio);
		buttonGroup.add(nativeRadio);
		compoundRadio.setSelected(true);
	}

	private void createNativeMetricPanel() {
		nativeMetricPanel = new ChooseNativeMetricPanel();
		nativeMetricPanel.addListListener(this);
		nativeMetricPanel.setVisible(false);
	}

	@Override
	protected Container buildPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(radioPanel(), BorderLayout.NORTH);
		panel.add(nativeMetricPanel, BorderLayout.CENTER);
		panel.add(buttonsPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel radioPanel() {
		JPanel radioPanel = new JPanel(new GridLayout(2, 1));
		radioPanel.add(compoundRadio);
		radioPanel.add(nativeRadio);
		return radioPanel;
	}

	private Component buttonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
		return buttonsPanel;
	}

	public Metric getMetric() {
		if (compoundRadio.isSelected())
			return new CompoundMetric();
		return nativeMetricPanel.get();
	}

	public void addOkListener(ActionListener listener) {
		okButton.addActionListener(listener);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == cancelButton)
			dispose();
		else if (source instanceof RadioButton) {
			nativeMetricPanel.setVisible(nativeRadio.isSelected());
			adjustSize();
			checkOkButton();
		}
	}

	@Override
	public void doubleClicked(NativeMetric metric) {
		okButton.doClick();
	}

	@Override
	public void selected(NativeMetric metric) {
		checkOkButton();
	}

	@Override
	public void selectionCleared() {
		checkOkButton();
	}

	private void checkOkButton() {
		okButton.setEnabled(compoundRadio.isSelected() || nativeMetricPanel.hasSelectedMetric());
	}
}