package org.kalibro.desktop.swingextension.field;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public abstract class NumberField<T extends Number> extends EditPanel<T> {

	private T specialNumber;
	private JFormattedTextField valueField;

	private GridBagPanelBuilder builder;

	public NumberField(String name) {
		super(name);
	}

	public NumberField(String name, T specialNumber) {
		this(name);
		this.specialNumber = specialNumber;
		addSpecialNumberButton();
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		valueField = new JFormattedTextField(new NumberFormatter(getDecimalFormat()));
		valueField.setName(getName());
		FieldSize fieldSize = new FieldSize(valueField);
		valueField.setSize(fieldSize);
		valueField.setHorizontalAlignment(SwingConstants.RIGHT);
		valueField.setColumns(getColumns());
	}

	public abstract DecimalFormat getDecimalFormat();

	protected abstract int getColumns();

	@Override
	protected void buildPanel() {
		builder = new GridBagPanelBuilder(this, 0);
		builder.add(valueField, 1.0);
	}

	private void addSpecialNumberButton() {
		String buttonTitle = getDecimalFormat().format(specialNumber);
		Button specialNumberButton = new Button(getName(), buttonTitle, this, "set", specialNumber);
		builder.add(new Label("  "));
		builder.add(specialNumberButton);
	}

	@Override
	public T get() {
		Number value = (Number) valueField.getValue();
		return (value == null) ? null : parseValue(value);
	}

	protected abstract T parseValue(Number value);

	@Override
	public void set(T value) {
		valueField.setValue(value);
	}
}